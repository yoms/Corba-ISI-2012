
package ws.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for post complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="post">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="pseudoEmetteur" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="contenu" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="date_heure" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="id_avatar" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "post", propOrder = {
    "id",
    "pseudoEmetteur",
    "contenu",
    "dateHeure",
    "idAvatar"
})
public class Post {

    protected int id;
    protected String pseudoEmetteur;
    protected String contenu;
    @XmlElement(name = "date_heure")
    protected String dateHeure;
    @XmlElement(name = "id_avatar")
    protected int idAvatar;

    /**
     * Gets the value of the id property.
     * 
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     */
    public void setId(int value) {
        this.id = value;
    }

    /**
     * Gets the value of the pseudoEmetteur property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPseudoEmetteur() {
        return pseudoEmetteur;
    }

    /**
     * Sets the value of the pseudoEmetteur property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPseudoEmetteur(String value) {
        this.pseudoEmetteur = value;
    }

    /**
     * Gets the value of the contenu property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContenu() {
        return contenu;
    }

    /**
     * Sets the value of the contenu property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContenu(String value) {
        this.contenu = value;
    }

    /**
     * Gets the value of the dateHeure property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateHeure() {
        return dateHeure;
    }

    /**
     * Sets the value of the dateHeure property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateHeure(String value) {
        this.dateHeure = value;
    }

    /**
     * Gets the value of the idAvatar property.
     * 
     */
    public int getIdAvatar() {
        return idAvatar;
    }

    /**
     * Sets the value of the idAvatar property.
     * 
     */
    public void setIdAvatar(int value) {
        this.idAvatar = value;
    }

}
