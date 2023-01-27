package ethereum.contract.app.backend.controller;

import ethereum.contract.app.backend.dto.ContractObjectDto;
import ethereum.contract.app.backend.dto.InputDto;
import ethereum.contract.app.backend.dto.TxResponseDto;
import ethereum.contract.app.backend.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contract")
public class ContractController {

    @Autowired
    private ContractService contractService;

    @PostMapping("/abi")
    public ResponseEntity importContract(@RequestBody String abiJson) {
        contractService.importContract(abiJson);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/functions")
    public ResponseEntity<List<ContractObjectDto>> getFunctions() {
        return ResponseEntity.ok().body(contractService.getFunctions());
    }

    @PostMapping("/functions/{abiFunctionName}")
    public ResponseEntity<TxResponseDto> executeFunction(@PathVariable("abiFunctionName") String functionName,
                                                         @RequestBody List<InputDto> inputs) {
        return ResponseEntity.ok().body(contractService.executeFunction(functionName, inputs));
    }

}

