package persistence.service;

import generic.persistence.GenericDao;

import org.springframework.stereotype.Repository;

import data.entity.Estadia;

@generic.persistence.Service(dataBase = "lar-amigos")
@Repository
public class EstadiaService extends GenericDao<Estadia> {
	
}
