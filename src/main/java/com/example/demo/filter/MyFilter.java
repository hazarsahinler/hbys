package com.example.demo.filter;

import com.example.demo.bus.jwt.JwtService;
import com.example.demo.dao.KullaniciDAO;
import com.example.demo.entity.Kullanici;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

@Component
public class MyFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final KullaniciDAO kullaniciDAO;

    public MyFilter(JwtService jwtService, KullaniciDAO kullaniciDAO) {
        this.jwtService = jwtService;
        this.kullaniciDAO = kullaniciDAO;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // Filtrenin devreye girmemesi gereken URL'leri burada belirtiyoruz
        String path = request.getRequestURI();
        return path.startsWith("/kullanici/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            JSONObject resp = new JSONObject();
            resp.put("success", false);
            resp.put("message","OTURUM ACMADAN BU ISLEMI YAPAMAZSINIZ.");
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(resp.toString());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String token = authHeader.substring(7);
        String kullaniciAdi = jwtService.extractUsername(token);

        if (kullaniciAdi != null && !kullaniciAdi.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {
            Kullanici kullanici = kullaniciDAO.kullaniciBilgiSorgu(kullaniciAdi);

            if (kullanici != null && jwtService.validateToken(token, kullaniciAdi)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(kullanici, null, kullanici.getRole().getAuthorities());
                authToken.setDetails(new org.springframework.security.web.authentication.WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                JSONObject resp = new JSONObject();
                resp.put("success", false);
                resp.put("message","OTURUM ZAMAN AŞIMINA UGRADI.LUTFEN TEKRAR GIRIS YAPINIZ.");
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(resp.toString());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        } else {
            JSONObject resp = new JSONObject();
            resp.put("success", false);
            resp.put("message","BU ISLEMI YAPMAK ICIN YETKINIZ BULUNMAMAKTADIR.");
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(resp.toString());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        filterChain.doFilter(request, response);
    }
}

