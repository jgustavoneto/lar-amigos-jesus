package persistence.service;

import generic.persistence.GenericDao;

import org.springframework.stereotype.Repository;

import data.entity.CategoriaBemConsumo;

@generic.persistence.Service(dataBase = "lar-amigos")
@Repository
public class TipoBemConsumoService  extends GenericDao<CategoriaBemConsumo>{

}
