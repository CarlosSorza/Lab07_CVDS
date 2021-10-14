package edu.eci.cvds.samples.services;

public class ExcepcionServiciosAlquiler extends Exception{
    public static final  String TARIFA_NEGATIVA = "La tarifa no debe ser negativa";
    public static final  String NO_ENCONTRADO_CLIENTE = "Ese cliente no se encuentra";
    public static final  String NO_ENCONTRADO_TIPOITEM = "Ese tipoItem no se encuentra";
    public static final  String DIAS_NEGATIVO = "Los días no pueden ser negativos";
    public static final  String NO_ENCONTRADO_ALQUILERITEM = "Ese Alquiler no se encuentra";
    public static final  String NO_ENCONTRADO_ITEM = "Ese item no se encuentra";
    public static final  String FECHA_LIMITE_NO_VALIDA = "La fecha es mayor a la de entrega";
    
    public ExcepcionServiciosAlquiler(String message){
        super(message);
    }

    public ExcepcionServiciosAlquiler(String message, Exception e){
        super(message,e);
    }
}