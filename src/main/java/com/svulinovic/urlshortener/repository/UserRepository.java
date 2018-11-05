package com.svulinovic.urlshortener.repository;

import com.svulinovic.urlshortener.model.RedirectUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public RedirectUrl getRedirectUrl(String code) {
        try {
            String sql = "select u.id, u.url, rs.redirectType from Url u inner join RedirectSettings rs on rs.urlId = u.id where rs.code = ?";
            return jdbcTemplate.queryForObject(sql, new Object[]{code}, (rs, rowNumber) -> {
                RedirectUrl redirectUrl = new RedirectUrl();
                redirectUrl.setUrlId(rs.getInt("id"));
                redirectUrl.setUrl(rs.getString("url"));
                redirectUrl.setRedirectType(rs.getInt("redirectType"));
                return redirectUrl;
            });
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    public void updateRedirectStatistics(Integer urlId) {
        Integer redirectCount = null;
        try {
            String sqlSelect = "select redirectCount from RedirectStatistics where urlId = ?";
            redirectCount = jdbcTemplate.queryForObject(sqlSelect, new Object[]{urlId}, Integer.class);
        } catch (Exception e) {}

        if (redirectCount == null) {
            String sqlInsert = "insert into RedirectStatistics (urlId, redirectCount) values (?, ?)";
            jdbcTemplate.update(sqlInsert, urlId, 1);
        } else {
            String sqlUpdate = "update RedirectStatistics set redirectCount = ? where urlId = ?";
            jdbcTemplate.update(sqlUpdate, redirectCount + 1, urlId);
        }
    }

}
