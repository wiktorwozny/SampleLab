package agh.edu.pl.slpbackend.service.iface;

public abstract class AbstractService {

    public abstract int insert(final IModel model);

    public abstract int update(final IModel model);

    public abstract int delete(final IModel model);
}
