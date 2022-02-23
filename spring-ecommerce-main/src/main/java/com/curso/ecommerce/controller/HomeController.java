package com.curso.ecommerce.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import com.curso.ecommerce.model.Cuentas;
import com.curso.ecommerce.model.Deposito;
import com.curso.ecommerce.model.Historial;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.service.IDetalleOrdenService;
import com.curso.ecommerce.service.IOrdenService;
import com.curso.ecommerce.service.IUsuarioService;
import com.curso.ecommerce.service.ProductoService;

@Controller
@RequestMapping("/")
public class HomeController {

	private final Logger log = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private IUsuarioService usuarioService;
	
	
	@Autowired
	private IOrdenService ordenService;
	
	@Autowired
	private IDetalleOrdenService detalleOrdenService;

	// para almacenar los detalles de la deposito
	List<Historial> detalles = new ArrayList<Historial>();

	// datos de la deposito
	Deposito deposito = new Deposito();

	@GetMapping("")
	public String home(Model model, HttpSession session) {
		
		log.info("Sesion del usuario: {}", session.getAttribute("idusuario"));
		
		model.addAttribute("productos", productoService.findAll());
		
		//session
		model.addAttribute("sesion", session.getAttribute("idusuario"));

		return "usuario/home";
	}

	@GetMapping("productohome/{id}")
	public String productoHome(@PathVariable Integer id, Model model) {
		log.info("Id cuentas enviado como parámetro {}", id);
		Cuentas cuentas = new Cuentas();
		Optional<Cuentas> productoOptional = productoService.get(id);
		cuentas = productoOptional.get();

		model.addAttribute("cuentas", cuentas);

		return "usuario/productohome";
	}

	@PostMapping("/cart")
	public String addCart(@RequestParam Integer id, @RequestParam Integer cantidad, Model model) {
		Historial detalleOrden = new Historial();
		Cuentas cuentas = new Cuentas();
		double sumaTotal = 0;

		Optional<Cuentas> optionalProducto = productoService.get(id);
		log.info("Cuentas añadido: {}", optionalProducto.get());
		log.info("Cantidad: {}", cantidad);
		cuentas = optionalProducto.get();

		detalleOrden.setCantidad(cantidad);
		detalleOrden.setPrecio(cuentas.getPrecio());
		detalleOrden.setNombre(cuentas.getNombre());
		detalleOrden.setTotal(cuentas.getPrecio() * cantidad);
		detalleOrden.setProducto(cuentas);
		
		//validar que le cuentas no se añada 2 veces
		Integer idProducto= cuentas.getId();
		boolean ingresado=detalles.stream().anyMatch(p -> p.getProducto().getId()==idProducto);
		
		if (!ingresado) {
			detalles.add(detalleOrden);
		}
		
		sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();

		deposito.setTotal(sumaTotal);
		model.addAttribute("cart", detalles);
		model.addAttribute("deposito", deposito);

		return "usuario/carrito";
	}

	// quitar un producto del carrito
	@GetMapping("/delete/cart/{id}")
	public String deleteProductoCart(@PathVariable Integer id, Model model) {

		// lista nueva de prodcutos
		List<Historial> ordenesNueva = new ArrayList<Historial>();

		for (Historial detalleOrden : detalles) {
			if (detalleOrden.getProducto().getId() != id) {
				ordenesNueva.add(detalleOrden);
			}
		}

		// poner la nueva lista con los productos restantes
		detalles = ordenesNueva;

		double sumaTotal = 0;
		sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();

		deposito.setTotal(sumaTotal);
		model.addAttribute("cart", detalles);
		model.addAttribute("deposito", deposito);

		return "usuario/carrito";
	}
	
	@GetMapping("/getCart")
	public String getCart(Model model, HttpSession session) {
		
		model.addAttribute("cart", detalles);
		model.addAttribute("deposito", deposito);
		
		//sesion
		model.addAttribute("sesion", session.getAttribute("idusuario"));
		return "/usuario/carrito";
	}
	
	@GetMapping("/order")
	public String order(Model model, HttpSession session) {
		
		Usuario usuario =usuarioService.findById( Integer.parseInt(session.getAttribute("idusuario").toString())).get();
		
		model.addAttribute("cart", detalles);
		model.addAttribute("deposito", deposito);
		model.addAttribute("usuario", usuario);
		
		return "usuario/resumenorden";
	}
	
	// guardar la deposito
	@GetMapping("/saveOrder")
	public String saveOrder(HttpSession session ) {
		Date fechaCreacion = new Date();
		deposito.setFechaCreacion(fechaCreacion);
		deposito.setNumero(ordenService.generarNumeroOrden());
		
		//usuario
		Usuario usuario =usuarioService.findById( Integer.parseInt(session.getAttribute("idusuario").toString())  ).get();
		
		deposito.setUsuario(usuario);
		ordenService.save(deposito);
		
		//guardar detalles
		for (Historial dt:detalles) {
			dt.setOrden(deposito);
			detalleOrdenService.save(dt);
		}
		
		///limpiar lista y deposito
		deposito = new Deposito();
		detalles.clear();
		
		return "redirect:/";
	}
	
	@PostMapping("/search")
	public String searchProduct(@RequestParam String nombre, Model model) {
		log.info("Nombre del producto: {}", nombre);
		List<Cuentas> cuentas = productoService.findAll().stream().filter(p -> p.getNombre().contains(nombre)).collect(Collectors.toList());
		model.addAttribute("cuentas", cuentas);
		return "usuario/home";
	}

}
