package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService {
	
	private DepartmentDao dao = DaoFactory.createDepartmentDao();
	
	public List<Department> findAll(){
		//vai no banco e busca os departamentos
		return dao.findAll();		
	}
	
	public void saveOrUpdate(Department obj) {
		if(obj.getId() == null) {
			//inserindo um novo department
			dao.insert(obj);
		}
		else {
			//atualizar
			dao.update(obj);
		}
	}
	

}
