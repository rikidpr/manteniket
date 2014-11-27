package an.dpr.manteniket;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import an.dpr.manteniket.dao.IComponentsDAO;
import an.dpr.manteniket.domain.Component;
import an.dpr.manteniket.template.ManteniketPage;
import de.agilecoders.wicket.core.markup.html.bootstrap.block.LabelBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.block.LabelType;
import de.agilecoders.wicket.core.markup.html.bootstrap.carousel.Carousel;
import de.agilecoders.wicket.core.markup.html.bootstrap.carousel.CarouselImage;
import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Alert;
import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Alert.Type;

public class MainPage extends ManteniketPage {

    private static final Logger log = LoggerFactory.getLogger(MainPage.class);
    @SpringBean
    private IComponentsDAO compDao;

    public MainPage() {
	super();
	alertas();

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

    private void alertas() {
	//TODO USAR LISTVIEW
	List<Component> alerts = compDao.getAlerts(getUser());
	ListView<Component> lv = new ListView<Component>("alerts", alerts){

	    private static final long serialVersionUID = 1L;

	    @Override
	    protected void populateItem(ListItem<Component> item) {
		Component c = item.getModelObject();
//		Label lbl = new Label("alert", c.getName());
		Alert alert = new Alert("alert", Model.of(c.getName()));
		Type labelType;
		double percentPass = new Double(c.getKmActual()-c.getKmAlert())*(new Double(100)/new Double(c.getKmAlert()));
		if(percentPass > 50){
		    labelType = Type.Danger;
		} else if (percentPass > 30){
		    labelType = Type.Warning;
		} else if (percentPass > 15){
		    labelType = Type.Info;
		} else {
		    labelType = Type.Success;
		}
//		lbl.add(new LabelBehavior(labelType));
//		item.add(lbl);
		alert.type(labelType);
		item.add(alert);
	    }
	};
	add(lv);
    }
    private void alertasRepeatingView() {
	//TODO USAR LISTVIEW
	RepeatingView rv = new RepeatingView("listAlerts");
	List<Component> alerts = compDao.getAlerts(getUser());
	for(Component c : alerts){
	    StringBuilder msg = new StringBuilder();
	    msg.append(c.getName()).append("-").append(c.getKmActual());
	    Label lbl = new Label(rv.newChildId(), Model.of(msg.toString()));
	    LabelType labelType;
	    double percentPass = new Double(c.getKmActual()-c.getKmAlert())*(new Double(100)/new Double(c.getKmAlert()));
	    log.debug(c.getName()+"-"+percentPass);
	    if(percentPass > 50){
		labelType = LabelType.Danger;
	    } else if (percentPass > 30){
		labelType = LabelType.Warning;
	    } else if (percentPass > 20){
		labelType = LabelType.Primary;
	    } else if (percentPass > 10){
		labelType = LabelType.Info;
	    } else {
		labelType = LabelType.Success;
	    }
	    lbl.add(new LabelBehavior(labelType));
	    rv.add(lbl);
	}
	add(rv);
    }
}
