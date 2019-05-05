package cn.nam.ssm.seckill.web;

import cn.nam.ssm.seckill.dto.Exposer;
import cn.nam.ssm.seckill.dto.SeckillExecution;
import cn.nam.ssm.seckill.dto.SeckillResult;
import cn.nam.ssm.seckill.entiy.Seckill;
import cn.nam.ssm.seckill.enums.SeckillStateEnum;
import cn.nam.ssm.seckill.exception.RepeatKillException;
import cn.nam.ssm.seckill.exception.SeckillCloseException;
import cn.nam.ssm.seckill.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 前端页面操作控制模块
 * 其他相关注解@Component @Service @Dao @Controller
 * 使用restul风格的url模块/资源/{}/细分
 *
 * @author Nanrong Zeng
 * @version 1.0
 */

@Component
@RequestMapping("/seckill")
public class SeckillController {

    @Autowired
    private SeckillService seckillService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model) {
        // TODO Model?
        List<Seckill> list = seckillService.getSeckillList();
        model.addAttribute("list", list);
        // 访问的是/WEB-INF/jps/list.jsp
        return "list";
    }

    @RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
        if (seckillId == null) {
            // 当没有点击具体某一秒杀商品详情时，重新返回list页面
            return "redirect:/seckill/list";
        }

        Seckill seckill = seckillService.getById(seckillId);
        if (seckill == null) {
            // TODO forward?
            return "forward:/seckill/list";
        }

        model.addAttribute("seckill", seckill);

        return "detail";
    }

    @RequestMapping(value = "/{seckillId}/exposer",
            method = RequestMethod.GET,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId) {

        SeckillResult<Exposer> result;
        try {
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<Exposer>(true, exposer);
        } catch (Exception e) {
            e.printStackTrace();
            result = new SeckillResult<Exposer>(false, e.getMessage());
        }

        return result;
    }

    @RequestMapping(value = "/{seckillId}/{md5}/execution",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(
            @PathVariable("seckillId") Long seckillId,
            @PathVariable("md5") String md5,
            @CookieValue(value = "userPhone", required = false) Long userPhone) {

        if (userPhone == null) {
            return new SeckillResult<SeckillExecution>(false, "未注册");
        }

        try {
            SeckillExecution successExecution =
                    seckillService.executeSeckill(seckillId, userPhone, md5);
            return new SeckillResult<SeckillExecution>(true, successExecution);
        } catch (RepeatKillException e1) {
            SeckillExecution repeatExecution = new
                    SeckillExecution(seckillId, SeckillStateEnum.REPEAT_KILL);
            return new SeckillResult<SeckillExecution>(true, repeatExecution);
        } catch (SeckillCloseException e2) {
            SeckillExecution endExecution = new
                    SeckillExecution(seckillId, SeckillStateEnum.END);
            return new SeckillResult<SeckillExecution>(true, endExecution);
        } catch (Exception e) {
            SeckillExecution innerErrorExecution = new
                    SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
            return new SeckillResult<SeckillExecution>(true, innerErrorExecution);
        }
    }

    @RequestMapping(value = "/time/now", method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> getCurrentTime() {
        Date now = new Date();
        return new SeckillResult<Long>(true, now.getTime());
    }

}
