package com.svulinovic.urlshortener.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

@Repository
public class ConfigurationRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean isAccountIdUnique(String accountId) {
        boolean isUnique = true;
        String sql = "select count(*) from UserAccount where accountId = ?";
        int count = jdbcTemplate.queryForObject(sql, new Object[]{accountId}, Integer.class);
        if (count > 0) {
            isUnique = false;
        }
        return isUnique;
    }

    public void saveAccount(String accountId, String password) {
        String sql = "insert into UserAccount (accountId, password) values (?, ?)";
        jdbcTemplate.update(sql, accountId, password);
    }

    public String getUrlCode(String url, Integer redirectType) {
        try {
            String sql = "select rs.code from RedirectSettings rs inner join Url u on u.id = rs.urlId where u.url = ? and rs.redirectType = ?";
            return jdbcTemplate.queryForObject(sql, new Object[]{url, redirectType}, String.class);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isUrlCodeUnique(String code) {
        boolean isUnique = true;
        String sql = "select count(*) from RedirectSettings where code = ?";
        int count = jdbcTemplate.queryForObject(sql, new Object[]{code}, Integer.class);
        if (count > 0) {
            isUnique = false;
        }
        return isUnique;
    }

    @Transactional
    public void saveUrlRedirectSettings(String url, Integer redirectType, String code) {

        Integer urlId = getUrlId(url);
        if (urlId == null) {
            SimpleJdbcInsert insertActor = new SimpleJdbcInsert(jdbcTemplate.getDataSource()).withTableName("Url").usingGeneratedKeyColumns("id");
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("url", url);
            urlId = insertActor.executeAndReturnKey(parameters).intValue();
        }
        String sqlRedirectSettings = "insert into RedirectSettings (urlId, redirectType, code) values (?, ?, ?)";
        jdbcTemplate.update(sqlRedirectSettings, urlId, redirectType, code);
    }

    private Integer getUrlId(String url) {
        try {
            String sql = "select id from Url where url = ?";
            return jdbcTemplate.queryForObject(sql, new Object[]{url}, Integer.class);
        } catch (Exception e) {
            return null;
        }
    }

    public Map<String, Integer> getStatistics() {
        try {
            String sql = "select u.url, isNull(rs.redirectCount, 0) redirectCount from Url u left join RedirectStatistics rs on rs.urlId = u.id group by u.id";
            return jdbcTemplate.query(sql, new Object[] {}, (ResultSet rs) -> {
                HashMap<String,Integer> results = new HashMap<>();
                while (rs.next()) {
                    results.put(rs.getString("url"), rs.getInt("redirectCount"));
                }
                return results;
            });
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

}
