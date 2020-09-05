package fr.scan.app.model;



import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Produit implements Serializable {

    private Integer pkId;
    private String reference;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate localDate;
    private Integer quantite;
    private BigDecimal prix;
    private boolean isNotAddition;

    public boolean isNotAddition() {
        return isNotAddition;
    }

    public void setNotAddition(boolean notAddition) {
        isNotAddition = notAddition;
    }

    public Integer getPkId() {
        return pkId;
    }

    public void setPkId(Integer pkId) {
        this.pkId = pkId;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public BigDecimal getPrix() {
        return prix;
    }


    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }



    @Override
    public String toString() {
        return "Produit{" +
                "pkId=" + pkId +
                ", reference='" + reference + '\'' +
                ", localDate=" + localDate +
                ", quantite=" + quantite +
                ", prix=" + prix +
                '}';
    }
}
