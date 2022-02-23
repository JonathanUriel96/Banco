package com.curso.ecommerce.controller;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import com.curso.ecommerce.model.Cuentas;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.service.IUsuarioService;
import com.curso.ecommerce.service.ProductoService;
import com.curso.ecommerce.service.UploadFileService;

@Controller
@RequestMapping("/productos")
public class CuentasController {
	
	private final Logger LOGGER = LoggerFactory.getLogger(CuentasController.class);
	
	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private UploadFileService upload;
	
	@GetMapping("")
	public String show(Model model) {
		model.addAttribute("productos", productoService.findAll());
		return "productos/show";
	}
	
	@GetMapping("/create")
	public String create() {
		return "productos/create";
	}
	
	@PostMapping("/save")
	public String save(Cuentas cuentas, @RequestParam("img") MultipartFile file, HttpSession session) throws IOException {
		LOGGER.info("Este es el objeto cuentas {}", cuentas);
		
		
		Usuario u= usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString() )).get();
		cuentas.setUsuario(u);
		
		//imagen
		if (cuentas.getId()==null) { // cuando se crea un cuentas
			String nombreImagen= upload.saveImage(file);
			cuentas.setImagen(nombreImagen);
		}else {
			
		}
		
		productoService.save(cuentas);
		return "redirect:/productos";
	}
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Integer id, Model model) {
		Cuentas cuentas = new Cuentas();
		Optional<Cuentas> optionalProducto=productoService.get(id);
		cuentas = optionalProducto.get();
		
		LOGGER.info("Cuentas buscado: {}", cuentas);
		model.addAttribute("cuentas", cuentas);
		
		return "productos/edit";
	}
	
	@PostMapping("/update")
	public String update(Cuentas cuentas, @RequestParam("img") MultipartFile file ) throws IOException {
		Cuentas p= new Cuentas();
		p=productoService.get(cuentas.getId()).get();
		
		if (file.isEmpty()) { // editamos el cuentas pero no cambiamos la imagem
			
			cuentas.setImagen(p.getImagen());
		}else {// cuando se edita tbn la imagen			
			//eliminar cuando no sea la imagen por defecto
			if (!p.getImagen().equals("default.jpg")) {
				upload.deleteImage(p.getImagen());
			}
			String nombreImagen= upload.saveImage(file);
			cuentas.setImagen(nombreImagen);
		}
		cuentas.setUsuario(p.getUsuario());
		productoService.update(cuentas);
		return "redirect:/productos";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Integer id) {
		
		Cuentas p = new Cuentas();
		p=productoService.get(id).get();
		
		//eliminar cuando no sea la imagen por defecto
		if (!p.getImagen().equals("default.jpg")) {
			upload.deleteImage(p.getImagen());
		}
		
		productoService.delete(id);
		return "redirect:/productos";
	}
	
	
}
