package com.tungstun.barapi.presentation.controllers;

import com.tungstun.barapi.application.OrderService;
import com.tungstun.barapi.domain.payment.Bill;
import com.tungstun.barapi.domain.payment.Order;
import com.tungstun.barapi.presentation.dto.converter.BillConverter;
import com.tungstun.barapi.presentation.dto.converter.OrderConverter;
import com.tungstun.barapi.presentation.dto.request.OrderRequest;
import com.tungstun.barapi.presentation.dto.response.BillResponse;
import com.tungstun.barapi.presentation.dto.response.OrderResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/api/bars/{barId}/")
public class OrderController {
    private final OrderService orderService;
    private final OrderConverter orderConverter;
    private final BillConverter billConverter;

    public OrderController(OrderService orderService, OrderConverter orderConverter, BillConverter billConverter) {
        this.orderService = orderService;
        this.orderConverter = orderConverter;
        this.billConverter = billConverter;
    }

    @GetMapping("orders")
    @PreAuthorize("hasPermission(#barId, {'ROLE_BAR_OWNER','ROLE_BARTENDER'})")
    @ApiOperation(
            value = "Finds all orders of bar",
            notes = "Provide id of bar to look up all orders that are linked to the bar",
            response = OrderResponse.class,
            responseContainer = "List"
    )
    public ResponseEntity<List<OrderResponse>> getAllBarOrders(
            @ApiParam(value = "ID value for the bar you want to retrieve orders from") @PathVariable("barId") Long barId
    ) throws EntityNotFoundException {
        List<Order> orders = this.orderService.getAllOrdersOfBar(barId);
        return new ResponseEntity<>(orderConverter.convertAll(orders), HttpStatus.OK);
    }

    @GetMapping("sessions/{sessionId}/orders")
    @PreAuthorize("hasPermission(#barId, {'ROLE_BAR_OWNER','ROLE_BARTENDER'})")
    @ApiOperation(
            value = "Finds all orders of session of bar",
            notes = "Provide id of bar and session to look up all orders that are linked session of the bar",
            response = OrderResponse.class,
            responseContainer = "List"
    )
    public ResponseEntity<List<OrderResponse>> getAllSessionOrders(
            @ApiParam(value = "ID value for the bar you want to retrieve orders from") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the session you want to retrieve orders from") @PathVariable("sessionId") Long sessionId
    ) throws EntityNotFoundException {
        List<Order> orders = this.orderService.getAllOrdersOfSession(barId, sessionId);
        return new ResponseEntity<>(orderConverter.convertAll(orders), HttpStatus.OK);
    }

    @GetMapping("sessions/{sessionId}/orders/{orderId}")
    @PreAuthorize("hasPermission(#barId, {'ROLE_BAR_OWNER','ROLE_BARTENDER'})")
    @ApiOperation(
            value = "Finds order of session of bar",
            notes = "Provide id of bar, session and order to look up specific order of session of the bar",
            response = OrderResponse.class
    )
    public ResponseEntity<OrderResponse> getOrderFromSession(
            @ApiParam(value = "ID value for the bar you want to retrieve the order from") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the session you want to retrieve the order from") @PathVariable("sessionId") Long sessionId,
            @ApiParam(value = "ID value for the order you want to retrieve") @PathVariable("orderId") Long orderId
    ) throws EntityNotFoundException {
        Order order = this.orderService.getOrderOfSession(barId, sessionId, orderId);
        return new ResponseEntity<>(orderConverter.convert(order), HttpStatus.OK);
    }

    @GetMapping("sessions/{sessionId}/bills/{billId}/orders")
    @PreAuthorize("hasPermission(#barId, {'ROLE_BAR_OWNER','ROLE_BARTENDER'})")
    @ApiOperation(
            value = "Finds orders of bill of session of bar",
            notes = "Provide id of bar, session, bill and order to look up orders of bill of session of the bar",
            response = OrderResponse.class
    )
    public ResponseEntity<List<OrderResponse>> getAllBillOrders(
            @ApiParam(value = "ID value for the bar you want to retrieve orders from") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the session you want to retrieve orders from") @PathVariable("sessionId") Long sessionId,
            @ApiParam(value = "ID value for the bill you want to retrieve orders from") @PathVariable("billId") Long billId
    ) throws EntityNotFoundException {
        List<Order> orders = this.orderService.getAllOrdersOfBill(barId, sessionId, billId);
        return new ResponseEntity<>(orderConverter.convertAll(orders), HttpStatus.OK);
    }

    @GetMapping("sessions/{sessionId}/bills/{billId}/orders/{orderId}")
    @PreAuthorize("hasPermission(#barId, {'ROLE_BAR_OWNER','ROLE_BARTENDER'})")
    @ApiOperation(
            value = "Finds order of bill of session of bar",
            notes = "Provide id of bar, session, bill and order to look up specific order of bill of session of the bar",
            response = OrderResponse.class
    )
    public ResponseEntity<OrderResponse> getOrderFromBill(
            @ApiParam(value = "ID value for the bar you want to retrieve the order from") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the session you want to retrieve the order from") @PathVariable("sessionId") Long sessionId,
            @ApiParam(value = "ID value for the bill you want to retrieve the order from") @PathVariable("billId") Long billId,
            @ApiParam(value = "ID value for the order you want to retrieve") @PathVariable("orderId") Long orderId
    ) throws EntityNotFoundException {
        Order order = this.orderService.getOrderOfBill(barId, sessionId, billId, orderId);
        return new ResponseEntity<>(orderConverter.convert(order), HttpStatus.OK);
    }

    @DeleteMapping("sessions/{sessionId}/bills/{billId}/orders/{orderId}")
    @PreAuthorize("hasPermission(#barId, {'ROLE_BAR_OWNER','ROLE_BARTENDER'})")
    @ApiOperation(
            value = "Deletes order of bill of session of bar",
            notes = "Provide id of bar, session, bill and order to delete specific order of bill of session of the bar"
    )
    public ResponseEntity<Void> deleteOrder(
            @ApiParam(value = "ID value for the bar you want to delete the order from") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the session you want to delete the order from") @PathVariable("sessionId") Long sessionId,
            @ApiParam(value = "ID value for the bill you want to delete the order from") @PathVariable("billId") Long billId,
            @ApiParam(value = "ID value for the order you want to delete") @PathVariable("orderId") Long orderId
    ) throws EntityNotFoundException {
        this.orderService.deleteOrderFromBill(barId, sessionId, billId, orderId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("sessions/{sessionId}/bills/{billId}")
    @PreAuthorize("hasPermission(#barId, {'ROLE_BAR_OWNER','ROLE_BARTENDER'})")
    @ApiOperation(
            value = "Create new order for bill of session of bar",
            notes = "Provide id of bar, session and bill to create a new order with information from request body",
            response = OrderResponse.class
    )
    public ResponseEntity<BillResponse> createNewOrder(
            @ApiParam(value = "ID value for the bar you want to add the new order to") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the session you want to add the new order to") @PathVariable("sessionId") Long sessionId,
            @ApiParam(value = "ID value for the bill you want to add the new order to") @PathVariable("billId") Long billId,
            @Valid @RequestBody OrderRequest orderLineRequest,
            @ApiIgnore Authentication authentication
    ) throws EntityNotFoundException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Bill bill = this.orderService.addProductToBill(barId, sessionId, billId, orderLineRequest, userDetails.getUsername());
        return new ResponseEntity<>(billConverter.convert(bill), HttpStatus.OK);
    }
}
