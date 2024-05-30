package agh.edu.pl.slpbackend.controller.iface;

import agh.edu.pl.slpbackend.service.iface.AbstractService;
import agh.edu.pl.slpbackend.service.iface.IModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class AbstractController {

    protected ResponseEntity<Void> add(final IModel model, final AbstractService service) throws Exception {
        try {
            service.insert(model);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    protected ResponseEntity<Void> edit(final IModel model, final AbstractService service) throws Exception {
        try {
            service.update(model);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    protected ResponseEntity<Void> delete(final IModel model, final AbstractService service) throws Exception {
        try {
            service.delete(model);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
