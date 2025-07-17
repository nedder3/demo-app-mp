package com.example.demoappmp.controller;

import com.example.demoappmp.model.Producto;
import com.example.demoappmp.repository.ProductoRepository;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping("/")
    public String index(Model model){
        List<Producto> productos = productoRepository.findAll();
        model.addAttribute("productos",productos);
        return "index";
    }
    @PostMapping("/comprar/{id}")
    public RedirectView comprarProducto(@PathVariable Long id) throws MPException, MPApiException {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        // Crear ítem con más detalles
        PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                .id(producto.getId().toString()) // ID interno
                .title(producto.getNombre())
                .description(producto.getDescripcion())
                .pictureUrl("https://via.placeholder.com/150") // Imagen genérica, podés agregar un campo si querés
                .categoryId("others") // O algo como "games", "electronics", etc.
                .quantity(1)
                .currencyId("ARS")
                .unitPrice(BigDecimal.valueOf(producto.getPrecio()))
                .build();

        List<PreferenceItemRequest> items = new ArrayList<>();
        items.add(itemRequest);

        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(items)
                .build();

        PreferenceClient client = new PreferenceClient();
        Preference preference = client.create(preferenceRequest);

        return new RedirectView(preference.getInitPoint());
    }
}
