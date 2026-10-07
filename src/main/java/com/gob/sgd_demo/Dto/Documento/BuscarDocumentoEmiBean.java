package com.gob.sgd_demo.Dto.Documento;

public record BuscarDocumentoEmiBean(
        String sCoDependencia,
        String sCoEmpleado,
        String sTiAcceso,

        String sCoAnnio,
        String sEstadoDoc,
        String sPrioridadDoc,
        String sTipoDoc,
        String sExpediente,
        String sRefOrigen,
        String sDestinatario,
        String sElaboradoPor,
        String sTrabDestino,//YUAL
        String coEmpRec,
        String esFiltroFecha,
        String sFeEmiIni,
        String sFeEmiFin,
        /***************************************/
        String sTipoBusqueda,//0 Filtrar, 1 Buscar,2 Buscar Referencia
        /***************************************/
        //busqueda desde tabla hacia BD
        String sNumCorEmision,
        String sDeEmiReferencia,
        // String sFeEmiIni,
        // String sFeEmiFin,
        String sDeTipoDocAdm,
        String sNumDoc,
        String sNumDocRef,
        String sDeAsuM,
        String sBuscDestinatario,
        // String sBuscElaboraradoPor,
        String sBuscNroExpediente,
        boolean esIncluyeFiltro,
        String sCoAnnioBus,
        String coTema,
        String coEstMensajeria,
        String coEst,
        String deEst,


        String txtRemitente,
        String txtDestinatario,
        String txtDepEmiteBus,
        String txtRefOrigen,
        String deEmpRec,
        String txtElaboradoPor,
        boolean esIncluyeOficina,
        boolean esIncluyeProfesional,

        String sFirmadoPor,
        String sParaFirmar
) {
}
