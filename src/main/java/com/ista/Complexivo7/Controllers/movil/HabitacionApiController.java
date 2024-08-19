package com.ista.Complexivo7.Controllers.movil;

import com.ista.Complexivo7.model.Service.IHabitacionService;
import com.ista.Complexivo7.model.entity.Habitacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/hotel")
public class HabitacionApiController {


        @Autowired
        private IHabitacionService habitacionService;

        @GetMapping("/listar")
        public ResponseEntity<List<Habitacion>> listar() {
            List<Habitacion> habitaciones = habitacionService.findAll();
            return new ResponseEntity<>(habitaciones, HttpStatus.OK);
        }

        @PostMapping("/form")
        public ResponseEntity<Habitacion> guardarHabitacion(@RequestBody Habitacion habitacion) {
            Habitacion savedHabitacion = habitacionService.saveApi(habitacion);
            return new ResponseEntity<>(savedHabitacion, HttpStatus.CREATED);
        }

        @GetMapping("/form/{id}")
        public ResponseEntity<Habitacion> actualizar(@PathVariable(value = "id") Long id) {
            Habitacion habitacion = habitacionService.findOne(id);
            if (habitacion == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(habitacion, HttpStatus.OK);
        }

        @DeleteMapping("/eliminar/{id}")
        public ResponseEntity<Void> eliminar(@PathVariable(value = "id") Long id) {
            if (id != null && id > 0) {
                habitacionService.delete(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        @GetMapping("/ver/{id}")
        public ResponseEntity<Habitacion> verProduct(@PathVariable(value = "id") Long id) {
            Habitacion habitacion = habitacionService.findOne(id);
            if (habitacion == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(habitacion, HttpStatus.OK);
        }

        @GetMapping("/reporte")
        public ResponseEntity<List<Habitacion>> generarReporte(
                @RequestParam(required = false) String tipo,
                @RequestParam(required = false) Boolean estado,
                @RequestParam(required = false) String numHabitacion,
                @RequestParam(required = false) Double precioNoche) {
            List<Habitacion> habitacionFiltrada = habitacionService.findProductosByFiltros(numHabitacion, estado, tipo, precioNoche);
            if (habitacionFiltrada.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(habitacionFiltrada, HttpStatus.OK);
        }
    }

