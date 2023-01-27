package ethereum.contract.app.backend.dto.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import ethereum.contract.app.backend.dto.ContractDto;
import ethereum.contract.app.backend.dto.ContractObjectDto;
import ethereum.contract.app.backend.dto.FieldDto;
import ethereum.contract.app.backend.enums.FieldType;
import ethereum.contract.app.backend.enums.ObjectType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ethereum.contract.app.backend.enums.ObjectType.EVENT;
import static ethereum.contract.app.backend.enums.ObjectType.FUNCTION;

public class AbiDeserializer extends StdDeserializer<ContractDto> {

    public AbiDeserializer() {
        super(ContractDto.class);
    }

    @Override
    public ContractDto deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        ContractDto contract = new ContractDto();
        List<ContractObjectDto> contractObjects = new ArrayList<>();
        List<Map<String, Object>> abiList = jsonParser.readValueAs(new TypeReference<>() {});
        for (Map<String, Object> abiMap : abiList) {
            ContractObjectDto contractObject = new ContractObjectDto();
            contractObject.setType(ObjectType.getValue((String) abiMap.get("type")));
            contractObject.setPayable((Boolean) abiMap.get("payable"));
            contractObject.setStateMutability((String) abiMap.get("stateMutability"));
            if (EVENT.equals(contractObject.getType())) {
                contractObject.setAnonymous((Boolean) abiMap.get("anonymous"));
            }
            if (FUNCTION.equals(contractObject.getType())) {
                contractObject.setConstant((Boolean) abiMap.get("constant"));
                contractObject.setOutputs(mapFields(new ArrayList<Map<String, String>>((List) abiMap.get("outputs"))));
            }
            if (EVENT.equals(contractObject.getType()) || FUNCTION.equals(contractObject.getType())) {
                contractObject.setInputs(mapFields(new ArrayList<Map<String, String>>((List) abiMap.get("inputs"))));
                contractObject.setName((String) abiMap.get("name"));
            }
            contractObjects.add(contractObject);
        }
        contract.setContractObjects(contractObjects);
        return contract;
    }

    private List<FieldDto> mapFields(List<Map<String, String>> fieldList) {
        List<FieldDto> fields = new ArrayList<>();
        for (Map<String, String> fieldMap : fieldList) {
            FieldDto field = new FieldDto();
            field.setName(fieldMap.get("name"));
            field.setType(FieldType.getValue(fieldMap.get("type")));
            fields.add(field);
        }
        return fields;
    }

}
