package com.learning.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.learning.service.Repos;

@Repository
public class GitRepoDAO {
	
	@Autowired
	JdbcTemplate jdbcTemplate;

	public List<Repos> getRepoFromDb(String userId) {
		
		System.out.println("userId " + userId);
		
		String query = "SELECT	 REPO_NAME," +
								"STARS, " +
								"CREATE_DATE " +
						"FROM	 GIT_REPO_INFO " +
						"WHERE	 USER_ID = ?";
		
		Object[] queryParams = {userId};
		int[] argTypes = {Types.VARCHAR};
		
		List<Repos> repos = jdbcTemplate.query(query, queryParams, argTypes, new RowMapper<Repos>(){

			public Repos mapRow(ResultSet rs, int arg1) throws SQLException {
				
				Repos repo = new Repos();
				repo.setName(rs.getString("REPO_NAME"));
				repo.setStargazers_count(rs.getInt("STARS"));
				repo.setCreated_at(rs.getTimestamp("CREATE_DATE"));
				return repo;
			}
		});
		System.out.println("sss " + repos.size());
		return repos;
		
	}
	
	@Transactional("transactionManager")
	public void saveRepos(final List<Repos> repos, final String userId) {
		
		String query = "INSERT INTO GIT_REPO_INFO VALUES(?, ?, ?, ?)";
		
		jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
			
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Repos repo = repos.get(i);
				ps.setString(1, userId);
				ps.setString(2, repo.getName());
				ps.setInt(3, repo.getStargazers_count());
				ps.setTimestamp(4, repo.getCreated_at());
			}
			
			public int getBatchSize() {
				return repos.size();
			}
		});
		
	}
	
}
