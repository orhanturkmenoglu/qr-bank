package com.example.qr_bank.controller;

import com.example.qr_bank.dto.request.TransactionEasyAddressTransferRequestDTO;
import com.example.qr_bank.dto.request.TransactionOperationRequestDTO;
import com.example.qr_bank.dto.request.TransactionQROperationRequestDTO;
import com.example.qr_bank.dto.request.TransactionRequestDTO;
import com.example.qr_bank.dto.response.TransactionEasyAddressTransferResponseDTO;
import com.example.qr_bank.dto.response.TransactionOperationResponseDTO;
import com.example.qr_bank.dto.response.TransactionQROperationResponseDTO;
import com.example.qr_bank.dto.response.TransactionResponseDTO;
import com.example.qr_bank.service.TransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Transaction operations")
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(summary = "Send money",
            description = "Sends money from one account to another.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Money sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid transaction request")
    })
    @PostMapping("/send")
    public ResponseEntity<TransactionResponseDTO> sendMoney(
            @Parameter(description = "Transaction details for sending money", required = true)
            @Valid  @RequestBody TransactionRequestDTO requestDTO) {
        TransactionResponseDTO response = transactionService.sendMoney(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @Operation(summary = "Receive Money", description = "Confirms the receipt of money by providing transaction ID and receiver account IBAN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Money received successfully"),
            @ApiResponse(responseCode = "404", description = "Transaction not found or receiver account not found")
    })
    @GetMapping("/receiver")
    public ResponseEntity<TransactionResponseDTO> receiveMoney(
            @Parameter(description = "ID of the transaction", required = true)
            @RequestParam(value = "transactionId") String transactionId,
            @Parameter(description = "IBAN of the receiver's account", required = true)
            @RequestParam(value = "receiverAccountIban") String receiverAccountIban) {
        TransactionResponseDTO response = transactionService.receiverMoney(transactionId, receiverAccountIban);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Deposit Money", description = "Deposits money into an account based on the provided operation details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Money deposited successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid deposit request")
    })
    @PostMapping("/deposit")
    public ResponseEntity<TransactionOperationResponseDTO> depositMoney(
            @Parameter(description = "Deposit operation details", required = true)
            @Valid @RequestBody TransactionOperationRequestDTO transactionOperationRequestDTO) {
        TransactionOperationResponseDTO response = transactionService.depositMoney(transactionOperationRequestDTO);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Withdraw Money", description = "Withdraws money from an account based on the provided operation details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Money withdrawn successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid withdrawal request")
    })
    @PostMapping("/withdraw")
    public ResponseEntity<TransactionOperationResponseDTO> withdrawMoney(
            @Parameter(description = "Withdrawal operation details", required = true)
            @Valid @RequestBody TransactionOperationRequestDTO transactionOperationRequestDTO) {
        TransactionOperationResponseDTO response = transactionService.withdrawMoney(transactionOperationRequestDTO);
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Deposit Money", description = "Deposits money into an account based on the provided operation details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Money deposited successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid deposit request")
    })
    @PostMapping("/deposit/qr")
    public ResponseEntity<TransactionQROperationResponseDTO> depositWithQR(@RequestBody TransactionQROperationRequestDTO requestDTO) throws JsonProcessingException, JsonProcessingException {
        TransactionQROperationResponseDTO responseDTO = transactionService.depositWithQR(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @Operation(summary = "Withdraw Money", description = "Withdraws money from an account based on the provided operation details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Money withdrawn successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid withdrawal request")
    })
    @PostMapping("/withdraw/qr")
    public ResponseEntity<TransactionQROperationResponseDTO> withdrawWithQR(@RequestBody TransactionQROperationRequestDTO requestDTO) {
        TransactionQROperationResponseDTO responseDTO = transactionService.withdrawWithQR(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }


    @Operation(summary = "Easy Address Transfer", description = "Performs an easy address transfer between two accounts.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Easy address transfer completed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid transfer request")
    })
    @PostMapping("/easy-transfer")
    public ResponseEntity<TransactionEasyAddressTransferResponseDTO> easyAddressTransfer(
            @RequestBody @Valid TransactionEasyAddressTransferRequestDTO transferRequestDTO) {

        TransactionEasyAddressTransferResponseDTO responseDTO = transactionService.easyAddressTransfer(transferRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }
}
