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
 * 使用restul风格的url模块/资源/{}/细分
 *
 * @author Nanrong Zeng
 * @version 1.0
 */

@Component("SeckillController")
@RequestMapping("/seckill")
public class SeckillController {

    @Autowired
    private SeckillService seckillService;

    /**
     * 获取秒杀商品列表页
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model) {
        List<Seckill> list = seckillService.getSeckillList();
        model.addAttribute("list", list);
        // 访问的是/WEB-INF/jps/list.jsp
        return "list";
    }

    /**
     * 获取某一商品的秒杀详情页
     *
     * @param seckillId
     * @param model
     * @return
     */
    @RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
        if (seckillId == null) {
            // 当没有点击具体某一秒杀商品详情时，重新返回list页面
            return "redirect:/seckill/list";
        }

        Seckill seckill = seckillService.getById(seckillId);
        if (seckill == null) {
            // id无效，重新返回list页面
            return "redirect:/seckill/list";
        }

        model.addAttribute("seckill", seckill);

        return "detail";
    }

    /**
     * 获取商品秒杀操作详情地址(md5)
     *
     * @param seckillId
     * @return 秒杀开始，返回秒杀操作入口；秒杀未开始，返回系统时间和秒杀时间
     */
    @RequestMapping(value = "/{seckillId}/exposer", method = RequestMethod.GET,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId) {

        SeckillResult<Exposer> result;
        try {
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<>(true, exposer);
        } catch (Exception e) {
            e.printStackTrace();
            result = new SeckillResult<>(false, e.getMessage());
        }

        return result;
    }

    /**
     * 执行秒杀
     *
     * @param seckillId
     * @param md5 md5加密的秒杀地址段
     * @param userPhone
     * @return 秒杀成功，返回包含秒杀明细对象的SeckillResult; 否则，返回错误信息
     */
    @RequestMapping(value = "/{seckillId}/{md5}/execution", method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(
            @PathVariable("seckillId") Long seckillId,
            @PathVariable("md5") String md5,
            @CookieValue(value = "userPhone", required = false) Long userPhone) {

        if (userPhone == null) {
            return new SeckillResult<>(false, "未注册");
        }

        try {
            SeckillExecution successExecution =
                    seckillService.executeSeckill(seckillId, userPhone, md5);
            return new SeckillResult<>(true, successExecution);
        } catch (RepeatKillException e1) {
            SeckillExecution repeatExecution = new
                    SeckillExecution(seckillId, SeckillStateEnum.REPEAT_KILL);
            return new SeckillResult<>(true, repeatExecution);
        } catch (SeckillCloseException e2) {
            SeckillExecution endExecution = new
                    SeckillExecution(seckillId, SeckillStateEnum.END);
            return new SeckillResult<>(true, endExecution);
        } catch (Exception e) {
            SeckillExecution innerErrorExecution = new
                    SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
            return new SeckillResult<>(true, innerErrorExecution);
        }
    }

    /**
     * 获取系统当前时间
     *
     * @return
     */
    @RequestMapping(value = "/time/now", method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> getCurrentTime() {
        Date now = new Date();
        return new SeckillResult<>(true, now.getTime());
    }

}
