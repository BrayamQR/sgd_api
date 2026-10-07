package com.gob.sgd_demo.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("seg_usuarios1")
public class Usuario {
    @Id
    @Column("cod_user")
    private String codUser;

    @Column("cdes_user")
    private String cdesUser;

    @Column("dfec_cre")
    private LocalDateTime dfecCre;

    @Column("cuser_cre")
    private String cuserCre;

    @Column("cuser_mod")
    private String cuserMod;

    @Column("dfec_mod")
    private LocalDateTime dfecMod;

    @Column("cclave")
    private String cclave;

    @Column("cemp_codemp")
    private String cempCodemp;

    @Column("es_usuario")
    private String esUsuario;

    @Column("in_acceso")
    private String inAcceso;

    @Column("dfe_mod_clave")
    private LocalDateTime dfeModClave;

    @Column("nu_intento")
    private Short nuIntento;

    @Column("ccos_codcosto")
    private String ccosCodcosto;

    @Column("in_ad")
    private String inAd;

    public Usuario() {
    }

    public Usuario(String inAd, String ccosCodcosto, Short nuIntento, LocalDateTime dfeModClave, String inAcceso, String esUsuario, String cempCodemp, String cclave, LocalDateTime dfecMod, String cuserMod, String cuserCre, LocalDateTime dfecCre, String cdesUser, String codUser) {
        this.inAd = inAd;
        this.ccosCodcosto = ccosCodcosto;
        this.nuIntento = nuIntento;
        this.dfeModClave = dfeModClave;
        this.inAcceso = inAcceso;
        this.esUsuario = esUsuario;
        this.cempCodemp = cempCodemp;
        this.cclave = cclave;
        this.dfecMod = dfecMod;
        this.cuserMod = cuserMod;
        this.cuserCre = cuserCre;
        this.dfecCre = dfecCre;
        this.cdesUser = cdesUser;
        this.codUser = codUser;
    }

    public String getCodUser() {
        return codUser;
    }

    public void setCodUser(String codUser) {
        this.codUser = codUser;
    }

    public String getCdesUser() {
        return cdesUser;
    }

    public void setCdesUser(String cdesUser) {
        this.cdesUser = cdesUser;
    }

    public LocalDateTime getDfecCre() {
        return dfecCre;
    }

    public void setDfecCre(LocalDateTime dfecCre) {
        this.dfecCre = dfecCre;
    }

    public String getCuserCre() {
        return cuserCre;
    }

    public void setCuserCre(String cuserCre) {
        this.cuserCre = cuserCre;
    }

    public String getCuserMod() {
        return cuserMod;
    }

    public void setCuserMod(String cuserMod) {
        this.cuserMod = cuserMod;
    }

    public LocalDateTime getDfecMod() {
        return dfecMod;
    }

    public void setDfecMod(LocalDateTime dfecMod) {
        this.dfecMod = dfecMod;
    }

    public String getCclave() {
        return cclave;
    }

    public void setCclave(String cclave) {
        this.cclave = cclave;
    }

    public String getCempCodemp() {
        return cempCodemp;
    }

    public void setCempCodemp(String cempCodemp) {
        this.cempCodemp = cempCodemp;
    }

    public String getEsUsuario() {
        return esUsuario;
    }

    public void setEsUsuario(String esUsuario) {
        this.esUsuario = esUsuario;
    }

    public String getInAcceso() {
        return inAcceso;
    }

    public void setInAcceso(String inAcceso) {
        this.inAcceso = inAcceso;
    }

    public LocalDateTime getDfeModClave() {
        return dfeModClave;
    }

    public void setDfeModClave(LocalDateTime dfeModClave) {
        this.dfeModClave = dfeModClave;
    }

    public Short getNuIntento() {
        return nuIntento;
    }

    public void setNuIntento(Short nuIntento) {
        this.nuIntento = nuIntento;
    }

    public String getCcosCodcosto() {
        return ccosCodcosto;
    }

    public void setCcosCodcosto(String ccosCodcosto) {
        this.ccosCodcosto = ccosCodcosto;
    }

    public String getInAd() {
        return inAd;
    }

    public void setInAd(String inAd) {
        this.inAd = inAd;
    }
}
