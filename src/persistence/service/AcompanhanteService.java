package persistence.service;

import generic.persistence.GenericDao;
import org.springframework.stereotype.Repository;
import data.entity.Acompanhante;

@generic.persistence.Service(dataBase = "lar-amigos")
@Repository
public class AcompanhanteService extends GenericDao<Acompanhante> {

}
