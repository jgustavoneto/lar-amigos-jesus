package persistence.service;

import generic.persistence.GenericDao;

import org.springframework.stereotype.Repository;

import data.entity.BemConsumo;

@generic.persistence.Service(dataBase = "lar-amigos")
@Repository
public class BemConsumoService extends GenericDao<BemConsumo>{

}
