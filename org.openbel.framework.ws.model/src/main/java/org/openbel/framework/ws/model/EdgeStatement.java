package org.openbel.framework.ws.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Uniquely identifies an EdgeStatement
 *
 * <p>Java class for EdgeStatement complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="EdgeStatement">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="statement" type="{http://belframework.org/ws/schemas}Statement"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EdgeStatement", propOrder = {
        "id",
        "statement"
})
public class EdgeStatement {

    @XmlElement(required = true)
    protected String id;
    @XmlElement(required = true)
    protected Statement statement;

    /**
     * Gets the value of the id property.
     *
     * @return possible object is {@link String}
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     * @param value allowed object is {@link String}
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the statement property.
     *
     * @return possible object is {@link Statement}
     */
    public Statement getStatement() {
        return statement;
    }

    /**
     * Sets the value of the statement property.
     *
     * @param value allowed object is {@link Statement}
     */
    public void setStatement(Statement value) {
        this.statement = value;
    }
}
