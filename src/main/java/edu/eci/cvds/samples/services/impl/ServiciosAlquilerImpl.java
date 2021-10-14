package edu.eci.cvds.samples.services.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import edu.eci.cvds.sampleprj.dao.ClienteDAO;
import edu.eci.cvds.sampleprj.dao.ItemDAO;
import edu.eci.cvds.sampleprj.dao.PersistenceException;

import edu.eci.cvds.samples.entities.Cliente;
import edu.eci.cvds.samples.entities.Item;
import edu.eci.cvds.samples.entities.ItemRentado;
import edu.eci.cvds.samples.entities.TipoItem;
import edu.eci.cvds.samples.services.ExcepcionServiciosAlquiler;
import edu.eci.cvds.samples.services.ServiciosAlquiler;
import java.sql.Date;
import java.util.List;
import java.time.LocalDate;
import java.util.Optional;
import java.time.temporal.ChronoUnit;

@Singleton
public class ServiciosAlquilerImpl implements ServiciosAlquiler {

   @Inject
   private ItemDAO itemDAO;
   @Inject
   private ClienteDAO clienteDAO;
   @Inject
   private ItemRentadoDAO itemRentadoDAO;
   @Inject
    private TipoItemDAO tipoItemDAO;

   @Override
   public int valorMultaRetrasoxDia(int itemId) {
       throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public Cliente consultarCliente(long docu) throws ExcepcionServiciosAlquiler {
    try {
        Optional<Cliente> clienteOp= Optional.ofNullable( clienteDAO.load(docu) );
        clienteOp.orElseThrow(() -> new ExcepcionServiciosAlquiler(ExcepcionServiciosAlquiler.NO_ENCONTRADO_ITEM));
        return clienteOp.get();
     } 
     catch (PersistenceException persistenceException) {
        throw new ExcepcionServiciosAlquiler("Error al consultar el Cliente : "+docu," | ", persistenceException);
    }
    
   }

   @Override
   public List<ItemRentado> consultarItemsCliente(long idcliente) throws ExcepcionServiciosAlquiler {
    try{
        consultarCliente( idcliente);
        return itemRentadoDAO.consultarItemsRentados(idcliente);
    }
    catch (PersistenceException persistenceException) {
        throw new ExcepcionServiciosAlquiler("Error al consultar items rentados por :"+ idcliente ," | " , persistenceException);
    }
   }

   @Override
   public List<Cliente> consultarClientes() throws ExcepcionServiciosAlquiler {
    try{
        return clienteDAO.consultarClientes();
    }
    catch (PersistenceException persistenceException){
        throw new ExcepcionServiciosAlquiler("Error al consultar los clientes",persistenceException);
    }
   }

   @Override
   public Item consultarItem(int id) throws ExcepcionServiciosAlquiler {
    try {
        Optional<Item> itemOp= Optional.ofNullable(itemDAO.load(id) );
        itemOp.orElseThrow(() -> new ExcepcionServiciosAlquiler(ExcepcionServiciosAlquiler.NO_ENCONTRADO_ITEM));
        return itemOp.get();
    } catch (PersistenceException persistenceException) {
        throw new ExcepcionServiciosAlquiler("Error al consultar el item:"+ id," | " , persistenceException);
    }
   }

   @Override
   public List<Item> consultarItemsDisponibles() {
    try{
        return itemDAO.load();
    }
    catch (PersistenceException persistenceException){
        throw new ExcepcionServiciosAlquiler("Error al consultar Items los disponibles",persistenceException);
    }
   }

   @Override
   public long consultarMultaAlquiler(int iditem, Date fechaDevolucion) throws ExcepcionServiciosAlquiler {
       throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public TipoItem consultarTipoItem(int id) throws ExcepcionServiciosAlquiler {
    try {
        Optional<TipoItem> tipoItemOp = Optional.ofNullable( tipoItemDAO.load(id) );
        tipoItemOp.orElseThrow(() -> new ExcepcionServiciosAlquiler(ExcepcionServiciosAlquiler.NO_ENCONTRADO_TIPOITEM));
        return tipoItemOp.get();
    } catch (PersistenceException persistenceException) {
        throw new ExcepcionServiciosAlquiler("Error al consultar Tipo Item :" + id," | " , persistenceException);
    }
   }

   @Override
   public List<TipoItem> consultarTiposItem() throws ExcepcionServiciosAlquiler {
    try {
        return tipoItemDAO.loadTiposItems();
    } catch (PersistenceException persistenceException) {
        throw new ExcepcionServiciosAlquiler("Error al consultar Tipo Items.", persistenceException);
    }
   }

   @Transactional
   @Override
   public void registrarAlquilerCliente(Date date, long docu, Item item, int numdias) throws ExcepcionServiciosAlquiler {
    try {
        Cliente cliente = consultarCliente( docu );
        consultarItem( item.getId());
        if( numdias < 0 ){
            throw new ExcepcionServiciosAlquiler( ExcepcionServiciosAlquiler.DIAS_NEGATIVO);
        }
        clienteDAO.agregarItemRentado(docu,item.getId(),date, Date.valueOf(date.toLocalDate().plusDays(numdias)));
    }
        catch (PersistenceException persistenceException) {
        throw new ExcepcionServiciosAlquiler("Error al registrar el alquiler", persistenceException);
    }
   }

   @Override
   public void registrarCliente(Cliente c) throws ExcepcionServiciosAlquiler {
    try {
        clienteDAO.save(c);
    }
    catch (PersistenceException persistenceException) {
        throw new ExcepcionServiciosAlquiler("Error al añadir el cliente.", persistenceException);
    }
   }

   @Override
   public long consultarCostoAlquiler(int iditem, int numdias) throws ExcepcionServiciosAlquiler {
    Item item = consultarItem( iditem );
    if( numdias < 0){
        throw new ExcepcionServiciosAlquiler( ExcepcionServiciosAlquiler.DIAS_NEGATIVO);
    }
    return item.getTarifaxDia() * numdias;
   }

   @Override
   public void actualizarTarifaItem(int id, long tarifa) throws ExcepcionServiciosAlquiler {
    try{
        consultarItem( id );
        if( tarifa < 0 ){
            throw new ExcepcionServiciosAlquiler(ExcepcionServiciosAlquiler.TARIFA_NEGATIVA);
        }
        itemDAO.actualizarTarifa(id,tarifa);
    }
    catch(PersistenceException persistenceException){
        throw new ExcepcionServiciosAlquiler("Error al cambiar tarifa del item : " + id ," | ",persistenceException);
    }
   }

   @Transactional
   @Override
   public void registrarItem(Item i) throws ExcepcionServiciosAlquiler {
    try{
        itemDAO.save(i);
    }
    catch (PersistenceException persistenceException){
        throw new ExcepcionServiciosAlquiler("Error al registrar item ",persistenceException);
    } //To change body of generated methods, choose Tools | Templates.
   }

   
   @Transactional
   @Override
   public void vetarCliente(long docu, boolean estado) throws ExcepcionServiciosAlquiler {
    try{
        consultarCliente( docu );
        clienteDAO.vetar(docu,estado);
    }
    catch(PersistenceException persistenceException){
        throw new ExcepcionServiciosAlquiler("Error al vetar al cliente : " + docu , " | " ,persistenceException);
    } //To change body of generated methods, choose Tools | Templates.
   }
}