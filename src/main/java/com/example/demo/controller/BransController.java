package com.example.demo.controller;

import com.example.demo.bus.bransBus.BransService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/branslar")
public class BransController {
    private final BransService bransService;
    public BransController(BransService bransService) {
        this.bransService = bransService;
    }

    /**
     * Tum brans bilgileri.
     * @param response
     * @throws IOException
     */
    @GetMapping
    public void getAllHasta(HttpServletResponse response) throws IOException {
        JSONArray respObj = bransService.getAllBrans();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (respObj != null && respObj.size() > 0) {
            response.getWriter().write(respObj.toString());
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message", "No record found");
            response.getWriter().write(jsonObject.toString());
        }
    }

    /**
     * Requesten aldığımız parametreleri okuyum bus katmanına oradan dao katmanına gönderip, girilen parametreye göre
     * Brans bilgisi response ediyoruz.
     * @param httpServletRequest
     * @param httpServletResponse
     * @throws IOException
     */

    @RequestMapping("/search")
    public void searchBrans(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) throws IOException {

        String bransIsmi = httpServletRequest.getParameter("bransIsmi");
        String bransKodu = httpServletRequest.getParameter("bransKodu");
        String bransIdParam = httpServletRequest.getParameter("bransId");
        Integer bransId = null;

        if (bransIdParam != null && !bransIdParam.isEmpty()) {
            try {
                bransId = Integer.parseInt(bransIdParam);
            } catch (NumberFormatException e) {
                httpServletResponse.setContentType("application/json");
                httpServletResponse.setCharacterEncoding("UTF-8");
                JSONObject errorResponse = new JSONObject();
                errorResponse.put("error", "Invalid bransId format");
                httpServletResponse.getWriter().write(errorResponse.toString());
                return;
            }
        }

        JSONArray jsonArray = bransService.getByParam(bransId,bransIsmi,bransKodu);
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setCharacterEncoding("UTF-8");
        if (jsonArray != null && jsonArray.size() > 0) {
            httpServletResponse.getWriter().write(jsonArray.toString());
        }
        else{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message", "No record found");
        }



    }
    @RequestMapping("/delete")
    public void deleteBrans(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) throws IOException {
        String bransIsmi = httpServletRequest.getParameter("bransIsmi");
        String bransKodu = httpServletRequest.getParameter("bransKodu");
        String bransIdParam = httpServletRequest.getParameter("bransId");
        Integer bransId = null;

        if (bransIdParam != null && !bransIdParam.isEmpty()) {
            try {
                bransId = Integer.parseInt(bransIdParam);
            } catch (NumberFormatException e) {
                httpServletResponse.setContentType("application/json");
                httpServletResponse.setCharacterEncoding("UTF-8");
                JSONObject errorResponse = new JSONObject();
                errorResponse.put("error", "Invalid bransId format");
                httpServletResponse.getWriter().write(errorResponse.toString());
                return;
            }
        }
        JSONObject jsonObject = bransService.deleteByParam( bransId,  bransIsmi,  bransKodu);
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.getWriter().write(jsonObject.toString());
    }
    @RequestMapping("/insert")
    public void insertBrans(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) throws Exception {
        String bransIsmi = httpServletRequest.getParameter("bransIsmi");
        String bransKodu = httpServletRequest.getParameter("bransKodu");
        String bransParam = httpServletRequest.getParameter("bransId");
        Integer bransId = null;
        if (bransParam != null && !bransParam.isEmpty()) {
            try {
                bransId = Integer.parseInt(bransParam);
            } catch (NumberFormatException e) {
                httpServletResponse.setContentType("application/json");
                httpServletResponse.setCharacterEncoding("UTF-8");
                JSONObject errorResponse = new JSONObject();
                errorResponse.put("error", "Invalid bransId format");
                httpServletResponse.getWriter().write(errorResponse.toString());
                return;
            }
        }
        JSONObject jsonObject = bransService.insertBrans(bransId,bransIsmi, bransKodu);

        httpServletResponse.setContentType("application/json");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.getWriter().write(jsonObject.toString());
    }

}
