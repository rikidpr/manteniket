package an.dpr.manteniket.components;

import java.io.Serializable;

/**
 * Interface para acciones a tomar con ConfirmPanel
 * @author rsaez
 *
 */
public interface ConfirmAction extends Serializable{

    public void accept();
    public void cancel();
}
