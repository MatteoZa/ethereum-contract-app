package ethereum.contract.app.backend.service;

import ethereum.contract.app.backend.dto.*;
import ethereum.contract.app.backend.enums.FieldType;
import ethereum.contract.app.backend.exception.FunctionNotFoundException;
import ethereum.contract.app.backend.exception.MissingContractException;
import ethereum.contract.app.backend.util.AbiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ethereum.contract.app.backend.enums.ObjectType.FUNCTION;

@Slf4j
@Service
public class ContractService {

    private static final BigInteger NONCE = BigInteger.ONE;
    private static final BigInteger GAS_PRICE = BigInteger.valueOf(300);
    private static final BigInteger GAS_LIMIT = BigInteger.valueOf(2000);
    private static final String TO = "0xac03bb73b6a9e108530aff4df5077c2b3d481e5a";
    private static final Credentials CREDENTIALS = Credentials.create("afdfd9c3d2095ef696594f6cedcae59e72dcd697e2a7521b1578140422a4f890");

    private static ContractDto CONTRACT;

    @Autowired
    private AbiParser abiParser;

    public void importContract(String abiJson) {
        CONTRACT = abiParser.parseAbiContract(abiJson);
        log.info("ABI parsed and stored");
    }

    public List<ContractObjectDto> getFunctions() {
        if (CONTRACT == null) {
            throw new MissingContractException();
        }
        return CONTRACT.getContractObjects()
                .stream()
                .filter(contractObject -> FUNCTION.equals(contractObject.getType()))
                .collect(Collectors.toList());
    }

    public TxResponseDto executeFunction(String functionName, List<InputDto> inputs) {
        if (CONTRACT == null) {
            throw new MissingContractException();
        }
        Optional<ContractObjectDto> functionObject = CONTRACT.getContractObjects().stream()
                .filter(contractObject -> functionName.equals(contractObject.getName()))
                .findFirst();
        if (functionObject.isEmpty()) {
            throw new FunctionNotFoundException(functionName);
        } else {
            TxResponseDto txResponseDto = signFunction(functionObject.get(), inputs);
            log.info("Generated signed transaction: {}", txResponseDto.getTxHex());
            return txResponseDto;
        }
    }

    private TxResponseDto signFunction(ContractObjectDto functionObject, List<InputDto> inputValues) {
        List<Type> inputTypes = mapInputList(functionObject.getInputs(), inputValues);
        Function function = new Function(functionObject.getName(),
                inputTypes,
                Arrays.asList(new TypeReference<Bool>() {}));
        String encodedFunction = FunctionEncoder.encode(function);
        RawTransaction rawTransaction = RawTransaction.createTransaction(NONCE, GAS_PRICE, GAS_LIMIT, TO, encodedFunction);
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, CREDENTIALS);
        return new TxResponseDto(Numeric.toHexString(signedMessage));
    }

    private List<Type> mapInputList(List<FieldDto> inputFields, List<InputDto> inputValues) {
        List<Type> types = new ArrayList<>();
        for(InputDto input : inputValues) {
            Optional<FieldDto> fieldDto = inputFields.stream()
                    .filter(field -> input.getName().equals(field.getName()))
                    .findFirst();
            if (fieldDto.isPresent()) {
                Type type = getType(fieldDto.get().getType(), input.getValue());
                if (type != null) {
                    types.add(type);
                }
            }
        }
        return types;
    }

    private Type getType(FieldType fieldType, String value) {
        switch(fieldType) {
            case STRING:
                return new Utf8String(value);
            case ADDRESS:
                return new Address(value);
            case UINT8:
                return new Uint8(Integer.parseInt(value));
            case UINT256:
                return new Uint256(Integer.parseInt(value));
            case BOOL:
                return new Bool(Boolean.parseBoolean(value));
            default:
                log.error("Type not mapped");
                return null;
        }
    }

}
