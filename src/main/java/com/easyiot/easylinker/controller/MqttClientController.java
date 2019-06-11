package com.easyiot.easylinker.controller;

import com.easyiot.easylinker.model.MqttClient;
import com.easyiot.easylinker.service.MqttClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/mqttClient")
public class MqttClientController {
    @Autowired
    MqttClientService mqttClientService;

    /**
     * 设备列表
     *
     * @param modelAndView
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/list")
    public ModelAndView list(ModelAndView modelAndView, HttpServletRequest httpServletRequest) {
        if (httpServletRequest.getParameter("page") != null && httpServletRequest.getParameter("size") != null) {
            int page = Integer.parseInt(httpServletRequest.getParameter("page"));
            int size = Integer.parseInt(httpServletRequest.getParameter("size"));
            if (page <= 0) page = 0;
            if (size <= 0) size = 20;
            Page<MqttClient> mqttClientPage = mqttClientService.list(PageRequest.of(page, size));
            modelAndView.addObject("mqttClientPage", mqttClientPage);
        } else {
            Page<MqttClient> mqttClientPage = mqttClientService.list(PageRequest.of(0, 20));
            modelAndView.addObject("mqttClientPage", mqttClientPage);
        }
        modelAndView.setViewName("mqtt/list");
        return modelAndView;
    }


    /**
     * 设备详情
     *
     * @param modelAndView
     * @param httpServletRequest
     * @return
     */

    @RequestMapping(value = "/detail")
    public ModelAndView detail(ModelAndView modelAndView, HttpServletRequest httpServletRequest) {
        //clientId
        long clientId = Long.parseLong(httpServletRequest.getParameter("clientId"));
        if (clientId != 0) {
            modelAndView.addObject("client", mqttClientService.findOneById(clientId));
        }
        modelAndView.setViewName("mqtt/detail");
        return modelAndView;

    }

    /**
     * 转向视图
     *
     * @param modelAndView
     * @return
     */
    @RequestMapping(value = "/toAdd")
    public ModelAndView toAdd(ModelAndView modelAndView) {

        modelAndView.setViewName("mqtt/add");
        return modelAndView;

    }

    /**
     * 添加设备
     *
     * @param modelAndView
     * @param httpServletRequest
     * @return
     */

    @RequestMapping(value = "/add")
    public ModelAndView add(ModelAndView modelAndView, HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes) {

        String deviceDescribe = httpServletRequest.getParameter("deviceDescribe");
        String name = httpServletRequest.getParameter("name");

        if (name == null || deviceDescribe == null) {
            redirectAttributes.addFlashAttribute("tip_message", "请输入关键信息!");
            modelAndView.setViewName("redirect:/mqttClient/toAdd");
            return modelAndView;
        }
        MqttClient mqttClient = new MqttClient();
        mqttClient.setName(name);
        mqttClient.setDeviceDescribe(deviceDescribe);
        mqttClientService.save(mqttClient);
        redirectAttributes.addFlashAttribute("tip_message", "添加成功!");
        modelAndView.setViewName("redirect:/mqttClient/toAdd");
        return modelAndView;

    }


}
