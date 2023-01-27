package ethereum.contract.app.backend.util;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import ethereum.contract.app.backend.dto.ContractDto;
import ethereum.contract.app.backend.dto.deserializer.AbiDeserializer;
import ethereum.contract.app.backend.exception.BadAbiFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AbiParser {

    private static final JsonMapper MAPPER = new JsonMapper();

    public AbiParser() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(ContractDto.class, new AbiDeserializer());
        MAPPER.registerModule(module);
    }

    public ContractDto parseAbiContract(String abiJson) {
        try {
            return MAPPER.readValue(abiJson, ContractDto.class);
        } catch (Exception e) {
            log.error("Error while deserializing ABI contract {}", abiJson);
            throw new BadAbiFormatException();
        }
    }

}
