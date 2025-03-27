package com.vvelc.booking.interface_.rest.controller;

import com.vvelc.booking.application.service.BookingOrderService;
import com.vvelc.booking.interface_.rest.dto.BookingOrderRequest;
import com.vvelc.booking.interface_.rest.dto.BookingOrderResponse;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.UUID;

@Tag(name = "Booking Orders", description = "Operaciones relacionadas con órdenes de reserva")
@Path("/booking-orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookingOrderController {

    @Inject
    BookingOrderService bookingOrderService;

    @POST
    @Operation(summary = "Crear una nueva orden de reserva")
    @APIResponse(responseCode = "200", description = "Orden creada exitosamente", content = @Content(
            schema = @Schema(implementation = BookingOrderResponse.class)
    ))
    @RequestBody(
            content = @Content(
                    schema = @Schema(implementation = BookingOrderRequest.class)
            )
    )
    @APIResponse(responseCode = "404", description = "No se encontró la orden luego de crearla")
    public Response createBookingOrder(BookingOrderRequest request) {
        UUID id = bookingOrderService.createBookingOrder(
                request.roomId(),
                request.customerName(),
                request.checkIn(),
                request.checkOut()
        );

        return Response.ok(bookingOrderService.getBookingOrderById(id))
                .build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Obtener una orden de reserva por ID")
    @APIResponse(responseCode = "200", description = "Orden encontrada", content = @Content(
            schema = @Schema(implementation = BookingOrderResponse.class)
    ))
    @APIResponse(responseCode = "404", description = "Orden no encontrada")
    public Response getById(@PathParam("id") UUID id) {
        return Response.ok(bookingOrderService.getBookingOrderById(id))
                .build();
    }

    @GET
    @Operation(summary = "Obtener todas las órdenes de reserva")
    @APIResponse(responseCode = "200", description = "Lista de órdenes de reserva", content = @Content(
            schema = @Schema(type = SchemaType.ARRAY, implementation = BookingOrderResponse.class)
    ))
    public Response getAll() {
        return Response.ok(bookingOrderService.getAllBookingOrders())
                .build();
    }
}
