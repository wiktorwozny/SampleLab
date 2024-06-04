package agh.edu.pl.slpbackend.service.iface;

public abstract class AbstractService {

    public abstract Object insert(final IModel model);

    public abstract Object update(final IModel model);

    public abstract void delete(final IModel model);
}
