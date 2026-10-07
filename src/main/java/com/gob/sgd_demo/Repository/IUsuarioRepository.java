package com.gob.sgd_demo.Repository;

import com.gob.sgd_demo.Dto.Usuario.*;
import com.gob.sgd_demo.Entity.Usuario;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IUsuarioRepository extends CrudRepository<Usuario, String> {
    @Query("""
        SELECT 
            su.cod_user         AS cod_user, 
            emp.cemp_codemp     AS cod_emp,  
            emp.cemp_apepat     AS ape_pat, 
            emp.cemp_apemat     AS ape_mat, 
            emp.cemp_denom      AS de_nom, 
            emp.cemp_nu_dni     AS nu_dni 
        FROM idosgd.seg_usuarios1 as su 
            INNER JOIN idosgd.rhtm_per_empleados AS emp 
                ON su.cemp_codemp = emp.cemp_codemp 
        WHERE su.cod_user = :codUser
        """)
    Optional<UsuarioInfoDTO> obtenerInfoUsuario(@Param("codUser") String codUser);

    @Query("""
        SELECT
            cod_user     AS cod_user,
            cemp_codemp  AS cemp_codemp,
            cclave       AS de_password
        FROM idosgd.seg_usuarios1
        WHERE cod_user = :codUser
        """)
    Optional<UsuarioAuthDTO> obtenerCredenciales(@Param("codUser") String codUser);

    @Query("""
        SELECT
            su.cod_user     AS cod_user,
            so.cod_opc      AS cod_opc,
            so.des_opc      AS des_opc
        FROM idosgd.seg_usuarios1 AS su
            INNER JOIN idosgd.seg_user_aplica_opc AS suao
            	ON su.cod_user = suao.cod_user
            INNER JOIN idosgd.seg_opciones as so
            	ON so.cod_opc = suao.cod_opc
        WHERE su.cod_user = :codUser
            """)
    List<UsuarioPermisosDTO> obtenerPermisos(@Param("codUser") String codUser);

    @Query("""
            SELECT
                su.cod_user         AS cod_user,
                so.cod_opc          AS cod_opc,
                so.des_opc          AS des_opc,
                so.id_group_opc     AS id_group_opc,
                so.de_group_opc     AS de_group_opc,
                so.title_opc        AS title_opc,
                so.path_opc         AS path_opc,
                so.icon_opc         AS icon_opc,
                so.order_opc        AS order_opc
            FROM idosgd.seg_usuarios1 AS su
                INNER JOIN idosgd.seg_user_aplica_opc AS suao
                    ON su.cod_user = suao.cod_user
                INNER JOIN idosgd.seg_opciones as so
                    ON so.cod_opc = suao.cod_opc
            WHERE su.cod_user = :codUser
                AND so.id_group_opc IS NOT NULL
            ORDER BY so.order_opc ASC
            """) List<UsuarioMenuAccesoDTO> obtenerMenuAcceso(@Param("codUser") String codUser);

    @Query("""
            SELECT
                REPLACE(a.de_dir_emi, '\\', '|') AS de_dir_emi,
                a.in_tipo_doc AS in_tipo_doc,
                a.co_emp AS cemp_codemp,
                a.co_dep AS co_dep,
                idosgd.pk_sgd_descripcion_de_dependencia(a.co_dep) AS de_dep,
                idosgd.pk_sgd_descripcion_de_sigla_corta(a.co_dep) AS de_siglas_dep,
                a.co_loc AS co_local,
                idosgd.pk_sgd_descripcion_de_local(a.co_loc) AS de_local,
                a.de_dir_ane AS de_ruta_anexo,
                a.de_pie_pagina,
                a.ti_acceso,
                a.in_consulta AS ti_consulta,
                a.in_mail AS in_correo,
                a.in_carga_doc AS in_carga_doc_mesa_partes,
                a.in_firma,
                a.in_obs_docu AS in_obs_documento,
                a.in_revi_docu AS in_revi_documento,
                b.in_numero_mp AS in_numero_mp,
                b.in_mesa_partes AS in_mesa_partes,
                (SELECT
                    de_par
                FROM idosgd.tdtr_parametros
                WHERE
                    co_par = 'ID_IMG_PORTADA_SGD') AS name_img_portada_sgd,
                (SELECT
                    de_par
                FROM idosgd.tdtr_parametros
                WHERE
                    co_par = 'DIAS_EXPIRACION_CLAVE') AS dias_expiracion_clave,
                (SELECT
                    dep.co_dependencia
                FROM idosgd.rhtm_dependencia AS dep
                WHERE dep.in_mesa_partes = '1'
                    AND dep.in_baja = '0'
                    AND 0 < (
                        SELECT count(1)
                        FROM idosgd.tdtr_dependencia_mp AS dmp
                            WHERE dmp.co_dep = b.co_dependencia
                                AND dmp.es_eli = '0'
                                AND dmp.in_mp = '1')) AS co_dep_mp,
                a.ti_acceso_mp,
                a.in_consulta_mp AS ti_consulta_mp,
                a.in_mail_deriv AS in_email_deriv
            FROM idosgd.tdtx_config_emp AS a
            	INNER JOIN idosgd.rhtm_dependencia AS b
            		ON a.co_dep = b.co_dependencia
            WHERE a.co_dep = :codDep
              AND a.co_emp = :codEmp
            """)
    Optional<UsuarioConfigDTO> obtenerConfig(@Param("codDep") String codDep, @Param("codEmp") String codEmp);

}
