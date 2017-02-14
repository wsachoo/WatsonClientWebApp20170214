package com.example.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.dataobject.PrintDO;

@Repository
public class DBServiceImpl implements DBService {

	private static int rowId = 4;

	@Autowired
	@Qualifier("hikariPostgreSqlJdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	@Override
	public PrintDO insertAndDisplaySampleData() {

		int key = rowId++;

		String sql = "insert into PRINT(id, title, description, imgsrc, price, quan) values (?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, new Object[] { key, "Beautiful Earth", "Geographical wonders of mother earth",
				"/earth/usa/", 250, 1000 });

		sql = "select * from PRINT where id = ?";
		PrintDO objPrintDO = (PrintDO) jdbcTemplate.queryForObject(sql, new Object[] { key },
				new BeanPropertyRowMapper<PrintDO>(PrintDO.class));
		System.out.println("Retrieved values are: " + objPrintDO);

		return objPrintDO;
	}
}
