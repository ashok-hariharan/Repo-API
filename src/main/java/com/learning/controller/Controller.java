package com.learning.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.learning.dao.GitRepoDAO;
import com.learning.service.Repos;

@RestController
public class Controller {

	@Autowired
	GitRepoDAO gitRepoDAO;
	
	@RequestMapping(value="/getRepos/{userId}", method=RequestMethod.GET, produces="application/json")
	public Object getRepos(@PathVariable(name="userId") String userId) {
		
		List<Repos> repos = gitRepoDAO.getRepoFromDb(userId);
		
		if (repos == null || repos.isEmpty()) {
			String url = "https://api.github.com/users/" + userId +"/repos";
			RestTemplate r = new RestTemplate();
			HttpHeaders h = new HttpHeaders();
			h.add("content-type", "application/json");
			HttpEntity<Object> he = new HttpEntity<Object>(h);
			Repos[] forNow = r.getForObject(url, Repos[].class);
			System.out.println("Length " + forNow.length);
			System.out.println("Repos " + forNow.toString());
			//gitRepoDAO.saveRepos(Arrays.asList(forNow), userId);
			return forNow;
		} else {
			return repos;
		}
		
	}
	
	@RequestMapping(value="/starRepos/{userId}/{repoName}", method=RequestMethod.PUT, produces="application/json")
	public void starRepo(@PathVariable(name="userId") String userId, @PathVariable(name="repoName") String repoName) {
		String url = "https://api.github.com/user/starred/"+ userId +"/" + repoName +"?access_token=c7357ac40a214a9c598d5dd0457de368d90e62d6";
		RestTemplate r = new RestTemplate();
		HttpHeaders h = new HttpHeaders();
		h.add("content-type", "application/json");
		HttpEntity<Object> he = new HttpEntity<Object>(h);
		r.put(url, null);
	}
	
	@RequestMapping(value="/unStarRepos/{userId}/{repoName}", method=RequestMethod.DELETE)
	public void unStarRepo(@PathVariable(name="userId") String userId, @PathVariable(name="repoName") String repoName) {
		String url = "https://api.github.com/user/starred/"+ userId +"/" + repoName +"?access_token=c7357ac40a214a9c598d5dd0457de368d90e62d6";
		RestTemplate r = new RestTemplate();
		HttpHeaders h = new HttpHeaders();
		h.add("content-type", "application/json");
		HttpEntity<Object> he = new HttpEntity<Object>(h);
		r.delete(url);
	}
		
}
