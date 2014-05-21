package persistence.service;

import generic.persistence.GenericDao;
import org.springframework.stereotype.Repository;
import data.entity.CategoriaMolestia;

@generic.persistence.Service(dataBase = "lar-amigos")
@Repository
public class CategoriaMolestiaService extends GenericDao<CategoriaMolestia> {

}
