package an.dpr.manteniket.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "components")
public class Component implements Serializable{

    private Long id;
    private String name;
    private String type;
    private String description;
    private Set<ComponentUse> componentUses;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    public Long getId() {
	return id;
    }

    public void setId(Long idComponente) {
	this.id= idComponente;
    }


    @OneToMany(fetch=FetchType.LAZY, mappedBy="component")
    public Set<ComponentUse> getComponentUses() {
        return componentUses;
    }

    public void setComponentUses(Set<ComponentUse> componentUses) {
        this.componentUses = componentUses;
    }

    @Column(nullable=false, length=50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(nullable=false)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(nullable=true, length=300)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
