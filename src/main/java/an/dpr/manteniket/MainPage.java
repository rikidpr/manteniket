package an.dpr.manteniket;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import an.dpr.manteniket.dao.IComponentsDAO;
import an.dpr.manteniket.domain.Component;
import an.dpr.manteniket.template.ManteniketPage;
import de.agilecoders.wicket.core.markup.html.bootstrap.block.LabelBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.block.LabelType;
import de.agilecoders.wicket.core.markup.html.bootstrap.carousel.Carousel;
import de.agilecoders.wicket.core.markup.html.bootstrap.carousel.CarouselImage;

public class MainPage extends ManteniketPage {

    private static final Logger log = LoggerFactory.getLogger(MainPage.class);
    private Label lblPrueba;
    @SpringBean
    private IComponentsDAO compDao;

    public MainPage() {
	super();
	StringBuilder msg = new StringBuilder("componentes en alerta de uso: ");
	List<Component> alerts = compDao.getAlerts(getUser());
	for(Component c : alerts){
	    msg.append(c.getName());
	    msg.append("; ");
	}
	lblPrueba = new Label("prueba", Model.of(msg.toString()));
	log.debug("add label behaviour");
	lblPrueba.add(new LabelBehavior(LabelType.Success));
	add(lblPrueba);
	
	

	List<CarouselImage> images = new ArrayList<CarouselImage>();
	// ad images
	images.add(new CarouselImage("/img/img1.jpg", 
		/*new ResourceModel(*/"consejo1.header"/*).getObject()*/, 
		/*new ResourceModel(*/"consejo1.desc"/*).getObject()*/));
	images.add(new CarouselImage("/img/img2.jpg",
		/*new ResourceModel(*/"consejo2.header"/*).getObject()*/, 
		/*new ResourceModel(*/"consejo2.desc"/*).getObject()*/));
	images.add(new CarouselImage("/img/img3.jpg",
		/*new ResourceModel(*/"consejo3.header"/*).getObject()*/, 
		/*new ResourceModel(*/"consejo3.desc"/*).getObject()*/));
	Carousel carousel = new Carousel("carousel", images);
	add(carousel);

    }
}
