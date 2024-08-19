package com.ista.Complexivo7.Controllers;

import com.ista.Complexivo7.model.Service.IHabitacionService;
import com.ista.Complexivo7.model.Service.IUsuarioService;
import com.ista.Complexivo7.model.entity.Habitacion;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.Map;
@Controller
@RequestMapping(value = "/hotel")
public class HabitacionControllers {
    @Autowired
    private IHabitacionService habitacionService;

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/listar")
    public String listar(Model model){
        List<String> tipo = List.of("Normal", "Suit", "Doble", "Sencilla", "Individual");
        model.addAttribute("tipos", tipo);
        model.addAttribute("habitaciones", habitacionService.findAll());
        return "hotel/listar";
    }

    @GetMapping("/form")
    public String crear(Map<String, Object> model) {
        Habitacion habitacion = new Habitacion();
        model.put("habitacion", habitacion);
        List<String> tipo = List.of("Normal", "Suit", "Doble", "Sencilla", "Individual");
        model.put("tipos", tipo);
        return "/hotel/form";
    }

    @PostMapping("/form")
    public String guardarHabitacion(@ModelAttribute("habitacion") Habitacion habitacion) {
        // Verifica que el objeto habitacion tiene datos válidos
        System.out.println("Habitacion recibida: " + habitacion);

        // Guarda la habitación
        habitacionService.save(habitacion);

        // Redirige a la lista de habitaciones
        return "redirect:/hotel/listar";
    }



    @GetMapping("/form/{id}")
    public String actualizar(@PathVariable(value = "id") Long id, Map<String, Object> model) {
        Habitacion habitacion = habitacionService.findOne(id);
        List<String> tipo = List.of("Normal", "Suit", "Doble", "Sencilla", "Individual");
        model.put("tipos", tipo);
        if (habitacion == null) {
            return "redirect:/hotel/listar";
        }
        model.put("habitacion", habitacion);
        return "/hotel/form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable(value = "id") Long id) {
        if (id != null && id > 0) {
            habitacionService.delete(id);
        }
        return "redirect:/hotel/listar";
    }

    @GetMapping("/ver/{id}")
    public String verProduct(@PathVariable(value = "id") Long id, Map<String, Object> model) {
        Habitacion habitacion = habitacionService.findOne(id);
        if (habitacion == null) {
            return "redirect:/hotel/listar";
        }
        model.put("habitacion", habitacion);
        return "/hotel/ver";
    }

    @GetMapping("/reporte")
    public String generarReporte(@RequestParam(required = false) String tipo,
                                 @RequestParam(required = false) Boolean estado,
                                 @RequestParam(required = false) String numHabitacion,
                                 @RequestParam(required = false) Double precioNoche,
                                 Model model) {
        List<String> tipos = List.of("Normal", "Suit", "Doble", "Sencilla", "Individual");
        model.addAttribute("tipos", tipos);
        List<Habitacion> habitacionFiltrada = habitacionService.findProductosByFiltros(numHabitacion, estado, tipo, precioNoche);
        if (habitacionFiltrada.isEmpty()) {
            model.addAttribute("noHabitaciones", "No se encontraron Habitaciones con esas especificaciones.");
        }
        model.addAttribute("habitaciones", habitacionFiltrada);
        model.addAttribute("selectedTipo", tipo);
        model.addAttribute("selectedEstado", estado);
        model.addAttribute("selectednumHabitacion", numHabitacion);
        model.addAttribute("selectedprecioNoche", precioNoche);
        return "hotel/listar"; // Asegúrate de que esta vista esté configurada correctamente
    }

    @GetMapping("/reporte/excel")
    public void exportToExcel(@RequestParam(required = false) String tipo,
                              @RequestParam(required = false) Boolean estado,
                              @RequestParam(required = false) String numHabitacion,
                              @RequestParam(required = false) Double precioNoche,
                              HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=habitaciones.xlsx");

        List<Habitacion> habitacionFiltrada = habitacionService.findProductosByFiltros(numHabitacion, estado, tipo, precioNoche);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Reporte de Habitaciones");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Id");
        headerRow.createCell(1).setCellValue("Numero de Habitacion");
        headerRow.createCell(2).setCellValue("Tipo");
        headerRow.createCell(3).setCellValue("Estado");
        headerRow.createCell(4).setCellValue("Precio por Noche");

        int rowNum = 1;
        for (Habitacion habitacion : habitacionFiltrada) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(habitacion.getId());
            row.createCell(1).setCellValue(habitacion.getNumHabitacion());
            row.createCell(2).setCellValue(habitacion.getTipo());
            row.createCell(3).setCellValue(habitacion.getEstado() ? "Disponible" : "No Disponible");
            row.createCell(4).setCellValue(habitacion.getPrecioNoche());
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }

    @GetMapping("/reporte/pdf")
    public void exportToPDF(@RequestParam(required = false) String tipo,
                            @RequestParam(required = false) Boolean estado,
                            @RequestParam(required = false) String numHabitacion,
                            @RequestParam(required = false) Double precioNoche,
                            HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=habitaciones.pdf");

        List<Habitacion> habitacionFiltrada = habitacionService.findProductosByFiltros(numHabitacion, estado, tipo, precioNoche);

        PdfWriter pdfWriter = new PdfWriter(response.getOutputStream());
        PdfDocument pdfDoc = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDoc);

        document.add(new Paragraph("Reporte de Habitaciones"));
        Table table = new Table(5);
        table.addCell("Id");
        table.addCell("Numero de Habitacion");
        table.addCell("Tipo");
        table.addCell("Estado");
        table.addCell("Precio por Noche");

        for (Habitacion habitacion : habitacionFiltrada) {
            table.addCell(String.valueOf(habitacion.getId()));
            table.addCell(habitacion.getNumHabitacion());
            table.addCell(habitacion.getTipo());
            table.addCell(habitacion.getEstado() ? "Disponible" : "No Disponible");
            table.addCell(String.valueOf(habitacion.getPrecioNoche()));
        }

        document.add(table);
        document.close();
    }

}
