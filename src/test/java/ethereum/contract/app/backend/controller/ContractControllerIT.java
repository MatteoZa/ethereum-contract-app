package ethereum.contract.app.backend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static ethereum.contract.app.backend.util.Util.getResourceFileAsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
public class ContractControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void importContractIsSuccessful() throws Exception {
        String abiString = getResourceFileAsString("erc20.abi.json");
        MvcResult response = mockMvc.perform(post("/contract/abi")
                        .contentType("application/json")
                        .content(abiString))
                .andReturn();

        assertEquals(HttpStatus.OK.value(), response.getResponse().getStatus());
    }

    @Test
    public void importContractThrowException() throws Exception {
        String abiString = getResourceFileAsString("erc20.abi.ERROR.json");
        MvcResult response = mockMvc.perform(post("/contract/abi")
                        .contentType("application/json")
                        .content(abiString))
                .andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getResponse().getStatus());
        assertEquals("The ABI format provided is not supported", response.getResponse().getContentAsString());
    }

}

