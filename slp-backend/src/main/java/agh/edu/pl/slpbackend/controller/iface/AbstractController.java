package agh.edu.pl.slpbackend.controller.iface;

import agh.edu.pl.slpbackend.service.iface.AbstractService;
import agh.edu.pl.slpbackend.service.iface.IModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class AbstractController {

    protected ResponseEntity<Void> add(final IModel model, final AbstractService service) {
        service.insert(model);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    protected ResponseEntity<Void> edit(final IModel model, final AbstractService service) {
        service.insert(model);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    protected ResponseEntity<Void> delete(final IModel model, final AbstractService service) {
        service.insert(model);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
