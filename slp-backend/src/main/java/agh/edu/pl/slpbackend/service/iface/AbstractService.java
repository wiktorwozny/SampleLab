package agh.edu.pl.slpbackend.service.iface;

import org.springframework.http.ResponseEntity;

public abstract class AbstractService {

    public abstract ResponseEntity<?> insert(final IModel model);

    public abstract ResponseEntity<?> update(final IModel model);

    public abstract void delete(final IModel model);
}
