package com.gob.sgd_demo.Repository;

import com.gob.sgd_demo.Dto.Global.GenericBasicListDTO;
import com.gob.sgd_demo.Dto.Global.GenericDescListDTO;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IGenericListRepository extends Repository<GenericBasicListDTO, Void> {
    @Query("""
        SELECT
            dep.co_dependencia   AS cod,
            dep.de_dependencia   AS desc,
            dep.de_corta_depen   AS desc_corta
        FROM idosgd.rhtm_dependencia AS dep
            WHERE dep.co_dependencia IN (
                SELECT co_dependencia
                FROM idosgd.rhtm_dependencia
                WHERE in_baja = '0'
                    AND co_empleado = :codEmp
                UNION
                SELECT co_dependencia
                FROM idosgd.rhtm_per_empleados
                WHERE cemp_codemp = :codEmp
                UNION
                SELECT co_dep
                FROM idosgd.tdtx_dependencia_empleado
                WHERE co_emp = :codEmp
                    AND es_emp = '0'
                UNION
                SELECT co_dep AS cod_dep
                FROM idosgd.tdtr_permisos
                WHERE co_use = :codUser
                    AND es_act = '0'
                EXCEPT
                SELECT co_dep AS cod_dep
                    FROM idosgd.tdtr_permisos
                WHERE co_use = :codUser
                    AND es_act = '1'
            )
            AND dep.in_baja <> '1'
        ORDER BY  cod
        """)
    List<GenericDescListDTO> obtenerDependenciasAcceso(@Param("codEmp") String codEmp, @Param("codUser") String codUser);

    @Query("""
        SELECT
            cano_anoeje AS cod,
            cano_anoeje AS desc
        FROM idosgd.si_mae_ano_ejecucion
        ORDER BY 2 DESC
        """)
    List<GenericBasicListDTO> listarAnios();

    @Query("""
        SELECT
            co_est AS cod,
            de_est AS desc
        FROM idosgd.tdtr_estados
            WHERE de_tab = :deTab
        ORDER BY co_est
        """)
    List<GenericBasicListDTO> listarEstados(@Param("deTab") String deTab);

    @Query("""
        SELECT
            co_pri AS cod,
            de_pri AS desc
        FROM idosgd.tdtr_prioridad
        ORDER BY co_pri
        """)
    List<GenericBasicListDTO> listarPrioridad();

    @Query("""
        SELECT
            a.cdoc_tipdoc AS cod,
            UPPER(a.cdoc_desdoc) AS desc
        FROM idosgd.si_mae_tipo_doc AS a
            INNER JOIN idosgd.sitm_doc_dependencia AS b
                ON a.cdoc_tipdoc = b.co_tip_doc
        WHERE a.cdoc_indbaj ='0'
            AND b.co_dep= :codDep
        ORDER BY b.co_tip_doc
        """)
    List<GenericBasicListDTO> listarTipoDoc(@Param("codDep") String codDep);

    @Query("""
        SELECT
            DISTINCT a.co_emp_emi AS cod,
            CASE
                WHEN TRIM(e.cemp_apepat) = TRIM(e.cemp_apemat)
                    AND TRIM(e.cemp_apemat) = TRIM(e.cemp_denom)
                THEN TRIM(e.cemp_apepat)
        
                ELSE CONCAT_WS(
                    ' ',
                    NULLIF(TRIM(e.cemp_apepat), ''),
                    NULLIF(TRIM(e.cemp_apemat), ''),
                    NULLIF(TRIM(e.cemp_denom), '')
                )
            END AS desc
        FROM idosgd.tdtv_remitos AS a
            INNER JOIN  idosgd.rhtm_per_empleados AS e
                ON e.cemp_codemp=a.co_emp_emi
        WHERE  a.ti_emi='01'
            AND a.es_eli = '0'
            AND a.co_gru = '1'
            AND a.co_dep_emi = :codDep
        ORDER BY a.co_emp_emi
        """)
    List<GenericBasicListDTO> listarEmpFirma(@Param("codDep") String codDep);

    @Query("""
        SELECT
            co_dependencia AS cod,
            de_dependencia AS desc,
            de_corta_depen AS desc_corta
        FROM idosgd.rhtm_dependencia
        WHERE co_nivel <> '6'
            AND in_baja = '0'
        UNION
        SELECT
            co_dependencia AS cod,
            de_dependencia AS desc,
            de_corta_depen AS desc_corta
        FROM idosgd.rhtm_dependencia
        WHERE co_nivel = '6'
            AND in_baja = '0'
            AND (co_depen_padre IN (
                SELECT co_depen_padre
                FROM idosgd.rhtm_dependencia
                WHERE co_dependencia = :codDep)
                    OR :codDep IN (co_dependencia, co_depen_padre))
        ORDER BY cod
        """)
    List<GenericDescListDTO> listarDependencia(@Param("codDep") String codDep);

    @Query("""
    SELECT DISTINCT
        b.co_dependencia   AS cod,
        b.de_corta_depen   AS desc_corta,
        b.de_dependencia   AS desc
    FROM idosgd.tdtx_resumen_dep a
    JOIN idosgd.rhtm_dependencia b ON a.co_dep_ref = b.co_dependencia
    WHERE (:anio = '0' OR a.nu_ann = :anio)
      AND a.co_dep = :codDep
      AND a.ti_tab = 'R'
      AND a.ti_emi = '01'
      AND a.co_emp_res = (CASE WHEN :tiAcceso = '0'
                               THEN a.co_emp_res
                               ELSE :codEmp END)
    UNION
    SELECT DISTINCT
        a.co_dep_ref       AS cod,
        CAST(NULL AS varchar) AS desc_corta,
        CASE
          WHEN a.ti_emi = '02' THEN 'PROVEEDORES'
          WHEN a.ti_emi = '03' THEN 'CIUDADANOS'
          WHEN a.ti_emi = '04' THEN 'OTROS'
          WHEN a.ti_emi = '05' THEN 'PERSONALES'
        END                AS desc
    FROM idosgd.tdtx_resumen_dep a
    WHERE (:anio = '0' OR a.nu_ann = :anio)
      AND a.co_dep = :codDep
      AND a.ti_tab = 'R'
      AND a.ti_emi <> '01'
      AND a.co_emp_res = (CASE WHEN :tiAcceso = '0'
                               THEN a.co_emp_res
                               ELSE :codEmp END)
    ORDER BY cod
    """)
    List<GenericDescListDTO> listarRefOrigen(@Param("anio") String anio,
                                             @Param("codDep") String codDep,
                                             @Param("codEmp") String codEmp,
                                             @Param("tiAcceso") String tiAcceso);
    @Query("""
    SELECT DISTINCT
        a.co_dep_ref AS cod,
        b.de_dependencia AS desc,
        b.de_corta_depen AS desc_corta
    FROM idosgd.tdtx_resumen_dep AS a,
        idosgd.rhtm_dependencia AS b
    WHERE
        (:anio = '0' OR a.nu_ann = :anio)
        AND a.co_dep = :codDep
        AND a.ti_tab = 'D'
        AND a.ti_emi = '01'
        AND a.co_emp_res = (
            CASE
                WHEN :tiConsutta = '0'
                    OR :tiConsutta = '2'
                THEN a.co_emp_res
                ELSE :codEmp
            END
        )
        AND a.co_dep_ref = b.co_dependencia
    UNION
    SELECT DISTINCT
        a.co_dep_ref AS cod,
        CASE
            WHEN a.ti_emi = '02' THEN 'PROVEEDORES'
            WHEN a.ti_emi = '03' THEN 'CIUDADANOS'
            WHEN a.ti_emi = '04' THEN 'OTROS'
            WHEN a.ti_emi = '05' THEN 'PERSONALES'
        END AS desc,
        NULL AS desc_corta
    FROM idosgd.tdtx_resumen_dep AS a
    WHERE
        (:anio = '0' OR a.nu_ann = :anio)
        AND a.co_dep = :codDep
        AND a.ti_tab = 'D'
        AND a.ti_emi <> '01'
        AND a.co_emp_res = (
            CASE
                WHEN :tiConsutta = '0'
                    OR :tiConsutta = '2'
                THEN a.co_emp_res
                ELSE :codEmp
            END
        )
    ORDER BY cod
    """)
    List<GenericDescListDTO> listarDestinatario(@Param("anio") String anio,
                                                @Param("codDep") String codDep,
                                                @Param("codEmp") String codEmp,
                                                @Param("tiConsutta") String tiConsutta);

    @Query("""
    SELECT
        e.cemp_codemp AS cod,
        CASE
            WHEN TRIM(e.cemp_apepat) = TRIM(e.cemp_apemat)
                AND TRIM(e.cemp_apemat) = TRIM(e.cemp_denom)
            THEN TRIM(e.cemp_apepat)
    
            ELSE CONCAT_WS(
                ' ',
                NULLIF(TRIM(e.cemp_apepat), ''),
                NULLIF(TRIM(e.cemp_apemat), ''),
                NULLIF(TRIM(e.cemp_denom), '')
            )
        END AS desc
    FROM idosgd.rhtm_per_empleados e
    WHERE e.cemp_est_emp = '1'
      AND (
            e.cemp_co_depend = :codDep
                OR e.co_dependencia = :codDep
          )
    ORDER BY cod
    """)
    List<GenericBasicListDTO> listarEmpDestino(@Param("codDep") String codDep);

    @Query("""
    SELECT
        e.cemp_codemp AS cod,
        CASE
            WHEN TRIM(e.cemp_apepat) = TRIM(e.cemp_apemat)
                AND TRIM(e.cemp_apemat) = TRIM(e.cemp_denom)
            THEN TRIM(e.cemp_apepat)
    
            ELSE CONCAT_WS(
                ' ',
                NULLIF(TRIM(e.cemp_apepat), ''),
                NULLIF(TRIM(e.cemp_apemat), ''),
                NULLIF(TRIM(e.cemp_denom), '')
            )
        END AS desc
    FROM idosgd.rhtm_per_empleados AS e
    WHERE e.cemp_est_emp = '1'
        AND (
            e.cemp_co_depend IN (
                SELECT d.co_dependencia
                FROM idosgd.rhtm_dependencia AS d
                WHERE d.co_dependencia = :codDep
                    OR d.co_depen_padre = :codDep
            )
            OR e.co_dependencia = :codDep
        )
    UNION
    SELECT
        e.cemp_codemp AS cod,
        CASE
            WHEN TRIM(e.cemp_apepat) = TRIM(e.cemp_apemat)
                AND TRIM(e.cemp_apemat) = TRIM(e.cemp_denom)
            THEN TRIM(e.cemp_apepat)
    
            ELSE CONCAT_WS(
                ' ',
                NULLIF(TRIM(e.cemp_apepat), ''),
                NULLIF(TRIM(e.cemp_apemat), ''),
                NULLIF(TRIM(e.cemp_denom), '')
            )
        END AS desc
    FROM idosgd.rhtm_per_empleados AS e
    WHERE e.cemp_est_emp = '1'
        AND e.cemp_codemp IN (
            SELECT co_emp
            FROM idosgd.tdtx_dependencia_empleado
            WHERE co_dep = :codDep
                AND es_emp = '0'
        )
    ORDER BY cod;
    """)
    List<GenericBasicListDTO> listarEmpElabora(@Param("codDep") String codDep);

    @Query("""
    SELECT
        e.cemp_codemp AS cod,
        CASE
            WHEN TRIM(e.cemp_apepat) = TRIM(e.cemp_apemat)
                AND TRIM(e.cemp_apemat) = TRIM(e.cemp_denom)
            THEN TRIM(e.cemp_apepat)
            ELSE CONCAT_WS(
                ' ',
                NULLIF(TRIM(e.cemp_apepat), ''),
                NULLIF(TRIM(e.cemp_apemat), ''),
                NULLIF(TRIM(e.cemp_denom), '')
            )
        END AS desc
    FROM idosgd.rhtm_per_empleados AS e
    WHERE e.cemp_est_emp = '1'
        AND (
            e.cemp_co_depend IN (
                SELECT d.co_dependencia
                FROM idosgd.rhtm_dependencia AS d
                WHERE d.co_dependencia = :codDep
                    OR d.co_depen_padre = :codDep
            )
            OR e.co_dependencia = :codDep
        )
        AND e.cemp_codemp = :codEmp
    ORDER BY cod;
    """)
    List<GenericBasicListDTO> listarEmpElaboraByEmp(@Param("codDep") String codDep, @Param("codEmp") String codEmp);

    @Query("""
    SELECT
        co_tema AS cod,
        de_tema AS desc
    FROM idosgd.tdtr_tema
        WHERE ES_ELI = '0'
            AND CO_DEPENDENCIA = :codDep
    """)
    List<GenericBasicListDTO> listarTema(@Param("codDep") String codDep);
}
