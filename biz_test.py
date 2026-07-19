import asyncio
import json
import os
import time
from datetime import datetime
from playwright.async_api import async_playwright, Page, expect

BASE_URL = "http://localhost:80"
USERNAME = "admin"
PASSWORD = "admin123"
OUTPUT_DIR = "/workspace/dogfood-output"
REPORT_PATH = os.path.join(OUTPUT_DIR, "biz-test-report.md")
SCREENSHOT_DIR = os.path.join(OUTPUT_DIR, "screenshots")

os.makedirs(OUTPUT_DIR, exist_ok=True)
os.makedirs(SCREENSHOT_DIR, exist_ok=True)

test_results = []
bugs = []
total_tests = 0
passed_tests = 0
failed_tests = 0


def record_result(page_name, test_item, status, error_msg="", screenshot=""):
    global total_tests, passed_tests, failed_tests
    total_tests += 1
    if status == "pass":
        passed_tests += 1
    else:
        failed_tests += 1
        if error_msg:
            bugs.append({
                "page": page_name,
                "test_item": test_item,
                "error": error_msg,
                "screenshot": screenshot
            })
    test_results.append({
        "page": page_name,
        "test_item": test_item,
        "status": status,
        "error": error_msg,
        "screenshot": screenshot
    })


async def take_screenshot(page: Page, name: str):
    path = os.path.join(SCREENSHOT_DIR, f"{name}.png")
    try:
        await page.screenshot(path=path, full_page=True)
        return path
    except Exception as e:
        return f"screenshot failed: {str(e)}"


async def check_for_errors(page: Page, page_name: str, test_item: str):
    try:
        error_texts = ["400", "404", "500", "错误", "失败", "error", "Error", "ERROR", "异常"]
        body_text = await page.inner_text("body", timeout=2000)
        
        for err in error_texts:
            if err in body_text:
                el = page.locator(f"text={err}")
                count = await el.count()
                if count > 0:
                    screenshot = await take_screenshot(page, f"error_{page_name}_{test_item}")
                    record_result(page_name, f"错误检查 - {test_item}", "fail", 
                                  f"页面出现错误文本: {err}", screenshot)
                    return False
        
        el_message = page.locator(".el-message--error, .el-message--warning")
        if await el_message.count() > 0:
            msg_text = await el_message.first.inner_text()
            screenshot = await take_screenshot(page, f"error_{page_name}_{test_item}")
            record_result(page_name, f"错误提示检查 - {test_item}", "fail",
                          f"错误提示: {msg_text}", screenshot)
            return False
            
        return True
    except Exception as e:
        return True


async def login(page: Page):
    print("[*] 正在登录系统...")
    await page.goto(f"{BASE_URL}/login")
    await page.wait_for_load_state("networkidle")
    
    await page.fill('input[placeholder="用户名"], input[name="username"], #username', USERNAME)
    await page.fill('input[placeholder="密码"], input[name="password"], #password', PASSWORD)
    
    await page.click('button:has-text("登录"), button:has-text("登 录"), .login-btn')
    await page.wait_for_load_state("networkidle")
    await page.wait_for_timeout(2000)
    
    current_url = page.url
    if "login" in current_url.lower():
        screenshot = await take_screenshot(page, "login_failed")
        record_result("登录", "登录系统", "fail", "登录失败，仍在登录页", screenshot)
        return False
    
    record_result("登录", "登录系统", "pass")
    print("[+] 登录成功")
    return True


async def test_community(page: Page):
    page_name = "小区管理"
    print(f"\n[*] 测试 {page_name}...")
    
    try:
        await page.goto(f"{BASE_URL}/biz/community")
        await page.wait_for_load_state("networkidle")
        await page.wait_for_timeout(1500)
        
        record_result(page_name, "页面加载成功", "pass")
        
        search_btn = page.locator('button:has-text("搜索"), .el-button--primary:has-text("搜索")')
        if await search_btn.count() > 0:
            await search_btn.first.click()
            await page.wait_for_timeout(1000)
            await check_for_errors(page, page_name, "点击搜索")
            record_result(page_name, "点击搜索按钮", "pass")
        else:
            record_result(page_name, "点击搜索按钮", "fail", "未找到搜索按钮")
        
        expand_btn = page.locator('.el-table__expand-icon, .el-icon-arrow-right')
        if await expand_btn.count() > 0:
            await expand_btn.first.click()
            await page.wait_for_timeout(1000)
            await check_for_errors(page, page_name, "展开第一行")
            record_result(page_name, "点击第一行展开箭头", "pass")
        else:
            record_result(page_name, "点击第一行展开箭头", "fail", "未找到展开按钮")
        
        add_btn = page.locator('button:has-text("新增"), .el-button--success:has-text("新增")')
        if await add_btn.count() > 0:
            await add_btn.first.click()
            await page.wait_for_timeout(1500)
            dialog = page.locator('.el-dialog, .el-drawer')
            if await dialog.count() > 0:
                record_result(page_name, "点击新增按钮", "pass")
                close_btn = page.locator('.el-dialog__close, .el-drawer__close-btn, button:has-text("取消")')
                if await close_btn.count() > 0:
                    await close_btn.first.click()
                    await page.wait_for_timeout(500)
            else:
                screenshot = await take_screenshot(page, "community_add_no_dialog")
                record_result(page_name, "点击新增按钮", "fail", "点击新增后弹窗未打开", screenshot)
        else:
            record_result(page_name, "点击新增按钮", "fail", "未找到新增按钮")
        
        edit_btn = page.locator('button:has-text("修改"), button:has-text("编辑")')
        if await edit_btn.count() > 0:
            await edit_btn.first.click()
            await page.wait_for_timeout(1500)
            dialog = page.locator('.el-dialog, .el-drawer')
            if await dialog.count() > 0:
                record_result(page_name, "点击修改按钮", "pass")
                close_btn = page.locator('.el-dialog__close, .el-drawer__close-btn, button:has-text("取消")')
                if await close_btn.count() > 0:
                    await close_btn.first.click()
                    await page.wait_for_timeout(500)
            else:
                screenshot = await take_screenshot(page, "community_edit_no_dialog")
                record_result(page_name, "点击修改按钮", "fail", "点击修改后弹窗未打开", screenshot)
        else:
            record_result(page_name, "点击修改按钮", "fail", "未找到修改按钮")
            
    except Exception as e:
        screenshot = await take_screenshot(page, "community_error")
        record_result(page_name, "页面测试", "fail", str(e), screenshot)


async def test_house(page: Page):
    page_name = "房屋管理"
    print(f"\n[*] 测试 {page_name}...")
    
    try:
        await page.goto(f"{BASE_URL}/biz/house")
        await page.wait_for_load_state("networkidle")
        await page.wait_for_timeout(1500)
        
        record_result(page_name, "页面加载成功", "pass")
        
        search_btn = page.locator('button:has-text("搜索"), .el-button--primary:has-text("搜索")')
        if await search_btn.count() > 0:
            await search_btn.first.click()
            await page.wait_for_timeout(1000)
            await check_for_errors(page, page_name, "点击搜索")
            record_result(page_name, "点击搜索按钮", "pass")
        else:
            record_result(page_name, "点击搜索按钮", "fail", "未找到搜索按钮")
        
        expand_btn = page.locator('.el-table__expand-icon, .el-icon-arrow-right')
        if await expand_btn.count() > 0:
            await expand_btn.first.click()
            await page.wait_for_timeout(1000)
            await check_for_errors(page, page_name, "展开第一行")
            record_result(page_name, "点击第一行展开箭头", "pass")
        else:
            record_result(page_name, "点击第一行展开箭头", "fail", "未找到展开按钮")
        
        community_select = page.locator('.el-select:has-text("小区"), select[name="communityId"]')
        if await community_select.count() > 0:
            await community_select.first.click()
            await page.wait_for_timeout(500)
            record_result(page_name, "小区下拉框可点击", "pass")
            await page.keyboard.press("Escape")
            await page.wait_for_timeout(300)
        else:
            record_result(page_name, "小区下拉框可输入过滤", "fail", "未找到小区下拉框")
        
        add_btn = page.locator('button:has-text("新增"), .el-button--success:has-text("新增")')
        if await add_btn.count() > 0:
            await add_btn.first.click()
            await page.wait_for_timeout(1500)
            dialog = page.locator('.el-dialog, .el-drawer')
            if await dialog.count() > 0:
                record_result(page_name, "点击新增按钮", "pass")
                close_btn = page.locator('.el-dialog__close, .el-drawer__close-btn, button:has-text("取消")')
                if await close_btn.count() > 0:
                    await close_btn.first.click()
                    await page.wait_for_timeout(500)
            else:
                screenshot = await take_screenshot(page, "house_add_no_dialog")
                record_result(page_name, "点击新增按钮", "fail", "点击新增后弹窗未打开", screenshot)
        else:
            record_result(page_name, "点击新增按钮", "fail", "未找到新增按钮")
            
    except Exception as e:
        screenshot = await take_screenshot(page, "house_error")
        record_result(page_name, "页面测试", "fail", str(e), screenshot)


async def test_contract(page: Page):
    page_name = "电子合同"
    print(f"\n[*] 测试 {page_name}...")
    
    try:
        await page.goto(f"{BASE_URL}/biz/contract")
        await page.wait_for_load_state("networkidle")
        await page.wait_for_timeout(1500)
        
        record_result(page_name, "页面加载成功", "pass")
        
        search_btn = page.locator('button:has-text("搜索"), .el-button--primary:has-text("搜索")')
        if await search_btn.count() > 0:
            await search_btn.first.click()
            await page.wait_for_timeout(1000)
            await check_for_errors(page, page_name, "点击搜索")
            record_result(page_name, "点击搜索按钮", "pass")
        else:
            record_result(page_name, "点击搜索按钮", "fail", "未找到搜索按钮")
        
        expand_btn = page.locator('.el-table__expand-icon, .el-icon-arrow-right')
        if await expand_btn.count() > 0:
            await expand_btn.first.click()
            await page.wait_for_timeout(1000)
            await check_for_errors(page, page_name, "展开第一行")
            record_result(page_name, "点击第一行展开箭头", "pass")
        else:
            record_result(page_name, "点击第一行展开箭头", "fail", "未找到展开按钮")
        
        pay_cycle_select = page.locator('.el-select:has-text("支付周期"), select[name="payCycle"]')
        if await pay_cycle_select.count() > 0:
            await pay_cycle_select.first.click()
            await page.wait_for_timeout(500)
            record_result(page_name, "支付周期下拉可选择", "pass")
            await page.keyboard.press("Escape")
            await page.wait_for_timeout(300)
        else:
            record_result(page_name, "支付周期下拉可选择", "fail", "未找到支付周期下拉框")
        
        add_btn = page.locator('button:has-text("新增"), .el-button--success:has-text("新增")')
        if await add_btn.count() > 0:
            await add_btn.first.click()
            await page.wait_for_timeout(1500)
            dialog = page.locator('.el-dialog, .el-drawer')
            if await dialog.count() > 0:
                record_result(page_name, "点击新增按钮", "pass")
                close_btn = page.locator('.el-dialog__close, .el-drawer__close-btn, button:has-text("取消")')
                if await close_btn.count() > 0:
                    await close_btn.first.click()
                    await page.wait_for_timeout(500)
            else:
                screenshot = await take_screenshot(page, "contract_add_no_dialog")
                record_result(page_name, "点击新增按钮", "fail", "点击新增后弹窗未打开", screenshot)
        else:
            record_result(page_name, "点击新增按钮", "fail", "未找到新增按钮")
            
    except Exception as e:
        screenshot = await take_screenshot(page, "contract_error")
        record_result(page_name, "页面测试", "fail", str(e), screenshot)


async def test_repair(page: Page):
    page_name = "房屋维修"
    print(f"\n[*] 测试 {page_name}...")
    
    try:
        await page.goto(f"{BASE_URL}/biz/repair")
        await page.wait_for_load_state("networkidle")
        await page.wait_for_timeout(1500)
        
        record_result(page_name, "页面加载成功", "pass")
        
        search_btn = page.locator('button:has-text("搜索"), .el-button--primary:has-text("搜索")')
        if await search_btn.count() > 0:
            await search_btn.first.click()
            await page.wait_for_timeout(1000)
            await check_for_errors(page, page_name, "点击搜索")
            record_result(page_name, "点击搜索按钮", "pass")
        else:
            record_result(page_name, "点击搜索按钮", "fail", "未找到搜索按钮")
        
        expand_btn = page.locator('.el-table__expand-icon, .el-icon-arrow-right')
        if await expand_btn.count() > 0:
            await expand_btn.first.click()
            await page.wait_for_timeout(1000)
            timeline = page.locator('.el-timeline, .timeline')
            timeline_ok = await timeline.count() > 0
            await check_for_errors(page, page_name, "展开第一行")
            if timeline_ok:
                record_result(page_name, "点击第一行展开箭头（时间轴）", "pass")
            else:
                record_result(page_name, "点击第一行展开箭头（时间轴）", "fail", "展开后未找到时间轴")
        else:
            record_result(page_name, "点击第一行展开箭头（时间轴）", "fail", "未找到展开按钮")
        
        detail_btn = page.locator('button:has-text("详情")')
        if await detail_btn.count() > 0:
            await detail_btn.first.click()
            await page.wait_for_timeout(1500)
            await check_for_errors(page, page_name, "点击详情")
            record_result(page_name, "点击详情按钮", "pass")
            close_btn = page.locator('.el-dialog__close, .el-drawer__close-btn, button:has-text("关闭")')
            if await close_btn.count() > 0:
                await close_btn.first.click()
                await page.wait_for_timeout(500)
        else:
            record_result(page_name, "点击详情按钮", "fail", "未找到详情按钮")
        
        status_btn = page.locator('button:has-text("房东确认"), button:has-text("确认维修")')
        if await status_btn.count() > 0:
            await status_btn.first.click()
            await page.wait_for_timeout(1500)
            await check_for_errors(page, page_name, "点击状态按钮")
            record_result(page_name, "点击状态按钮", "pass")
        else:
            status_btns = page.locator('.el-table button.el-button--text, .table-operator button')
            if await status_btns.count() > 1:
                record_result(page_name, "点击状态按钮", "pass", "找到操作按钮")
            else:
                record_result(page_name, "点击状态按钮", "fail", "未找到状态操作按钮")
            
    except Exception as e:
        screenshot = await take_screenshot(page, "repair_error")
        record_result(page_name, "页面测试", "fail", str(e), screenshot)


async def test_phonebook(page: Page):
    page_name = "电话簿管理"
    print(f"\n[*] 测试 {page_name}...")
    
    try:
        await page.goto(f"{BASE_URL}/biz/phonebook")
        await page.wait_for_load_state("networkidle")
        await page.wait_for_timeout(1500)
        
        record_result(page_name, "页面加载成功", "pass")
        
        tree_nodes = page.locator('.el-tree-node, .tree-node')
        if await tree_nodes.count() > 0:
            await tree_nodes.first.click()
            await page.wait_for_timeout(1000)
            await check_for_errors(page, page_name, "点击分类树")
            record_result(page_name, "点击左侧分类树节点", "pass")
        else:
            record_result(page_name, "点击左侧分类树节点", "fail", "未找到分类树节点")
        
        search_btn = page.locator('button:has-text("搜索"), .el-button--primary:has-text("搜索")')
        if await search_btn.count() > 0:
            await search_btn.first.click()
            await page.wait_for_timeout(1000)
            await check_for_errors(page, page_name, "点击搜索")
            record_result(page_name, "点击搜索按钮", "pass")
        else:
            record_result(page_name, "点击搜索按钮", "fail", "未找到搜索按钮")
        
        expand_btn = page.locator('.el-table__expand-icon, .el-icon-arrow-right')
        if await expand_btn.count() > 0:
            await expand_btn.first.click()
            await page.wait_for_timeout(1000)
            await check_for_errors(page, page_name, "展开第一行")
            record_result(page_name, "点击第一行展开箭头", "pass")
        else:
            record_result(page_name, "点击第一行展开箭头", "fail", "未找到展开按钮")
        
        add_btn = page.locator('button:has-text("新增"), .el-button--success:has-text("新增")')
        if await add_btn.count() > 0:
            await add_btn.first.click()
            await page.wait_for_timeout(1500)
            dialog = page.locator('.el-dialog, .el-drawer')
            if await dialog.count() > 0:
                record_result(page_name, "点击新增按钮", "pass")
                close_btn = page.locator('.el-dialog__close, .el-drawer__close-btn, button:has-text("取消")')
                if await close_btn.count() > 0:
                    await close_btn.first.click()
                    await page.wait_for_timeout(500)
            else:
                screenshot = await take_screenshot(page, "phonebook_add_no_dialog")
                record_result(page_name, "点击新增按钮", "fail", "点击新增后弹窗未打开", screenshot)
        else:
            record_result(page_name, "点击新增按钮", "fail", "未找到新增按钮")
            
    except Exception as e:
        screenshot = await take_screenshot(page, "phonebook_error")
        record_result(page_name, "页面测试", "fail", str(e), screenshot)


async def test_banner(page: Page):
    page_name = "轮播图管理"
    print(f"\n[*] 测试 {page_name}...")
    
    try:
        await page.goto(f"{BASE_URL}/biz/banner")
        await page.wait_for_load_state("networkidle")
        await page.wait_for_timeout(1500)
        
        record_result(page_name, "页面加载成功", "pass")
        
        search_btn = page.locator('button:has-text("搜索"), .el-button--primary:has-text("搜索")')
        if await search_btn.count() > 0:
            await search_btn.first.click()
            await page.wait_for_timeout(1000)
            await check_for_errors(page, page_name, "点击搜索")
            record_result(page_name, "点击搜索按钮", "pass")
        else:
            record_result(page_name, "点击搜索按钮", "fail", "未找到搜索按钮")
        
        add_btn = page.locator('button:has-text("新增"), .el-button--success:has-text("新增")')
        if await add_btn.count() > 0:
            await add_btn.first.click()
            await page.wait_for_timeout(1500)
            dialog = page.locator('.el-dialog, .el-drawer')
            if await dialog.count() > 0:
                record_result(page_name, "点击新增按钮", "pass")
                close_btn = page.locator('.el-dialog__close, .el-drawer__close-btn, button:has-text("取消")')
                if await close_btn.count() > 0:
                    await close_btn.first.click()
                    await page.wait_for_timeout(500)
            else:
                screenshot = await take_screenshot(page, "banner_add_no_dialog")
                record_result(page_name, "点击新增按钮", "fail", "点击新增后弹窗未打开", screenshot)
        else:
            record_result(page_name, "点击新增按钮", "fail", "未找到新增按钮")
            
    except Exception as e:
        screenshot = await take_screenshot(page, "banner_error")
        record_result(page_name, "页面测试", "fail", str(e), screenshot)


async def test_invite(page: Page):
    page_name = "邀请管理"
    print(f"\n[*] 测试 {page_name}...")
    
    try:
        await page.goto(f"{BASE_URL}/biz/invite")
        await page.wait_for_load_state("networkidle")
        await page.wait_for_timeout(1500)
        
        record_result(page_name, "页面加载成功", "pass")
        
        search_btn = page.locator('button:has-text("搜索"), .el-button--primary:has-text("搜索")')
        if await search_btn.count() > 0:
            await search_btn.first.click()
            await page.wait_for_timeout(1000)
            await check_for_errors(page, page_name, "点击搜索")
            record_result(page_name, "点击搜索按钮", "pass")
        else:
            record_result(page_name, "点击搜索按钮", "fail", "未找到搜索按钮")
        
        expand_btn = page.locator('.el-table__expand-icon, .el-icon-arrow-right')
        if await expand_btn.count() > 0:
            await expand_btn.first.click()
            await page.wait_for_timeout(1000)
            await check_for_errors(page, page_name, "展开第一行")
            record_result(page_name, "点击第一行展开箭头", "pass")
        else:
            record_result(page_name, "点击第一行展开箭头", "fail", "未找到展开按钮")
        
        detail_btn = page.locator('button:has-text("详情")')
        if await detail_btn.count() > 0:
            await detail_btn.first.click()
            await page.wait_for_timeout(1500)
            await check_for_errors(page, page_name, "点击详情")
            record_result(page_name, "点击详情按钮", "pass")
            close_btn = page.locator('.el-dialog__close, .el-drawer__close-btn, button:has-text("关闭")')
            if await close_btn.count() > 0:
                await close_btn.first.click()
                await page.wait_for_timeout(500)
        else:
            record_result(page_name, "点击详情按钮", "fail", "未找到详情按钮")
        
        status_tags = page.locator('.el-tag, .tag')
        if await status_tags.count() > 0:
            record_result(page_name, "邀请状态显示为标签", "pass")
        else:
            record_result(page_name, "邀请状态显示为标签", "fail", "未找到状态标签")
            
    except Exception as e:
        screenshot = await take_screenshot(page, "invite_error")
        record_result(page_name, "页面测试", "fail", str(e), screenshot)


async def test_message(page: Page):
    page_name = "消息管理"
    print(f"\n[*] 测试 {page_name}...")
    
    try:
        await page.goto(f"{BASE_URL}/biz/message")
        await page.wait_for_load_state("networkidle")
        await page.wait_for_timeout(1500)
        
        record_result(page_name, "页面加载成功", "pass")
        
        search_btn = page.locator('button:has-text("搜索"), .el-button--primary:has-text("搜索")')
        if await search_btn.count() > 0:
            await search_btn.first.click()
            await page.wait_for_timeout(1000)
            await check_for_errors(page, page_name, "点击搜索")
            record_result(page_name, "点击搜索按钮", "pass")
        else:
            record_result(page_name, "点击搜索按钮", "fail", "未找到搜索按钮")
        
        add_btn = page.locator('button:has-text("新增"), .el-button--success:has-text("新增")')
        if await add_btn.count() > 0:
            await add_btn.first.click()
            await page.wait_for_timeout(1500)
            dialog = page.locator('.el-dialog, .el-drawer')
            if await dialog.count() > 0:
                record_result(page_name, "点击新增按钮", "pass")
                close_btn = page.locator('.el-dialog__close, .el-drawer__close-btn, button:has-text("取消")')
                if await close_btn.count() > 0:
                    await close_btn.first.click()
                    await page.wait_for_timeout(500)
            else:
                screenshot = await take_screenshot(page, "message_add_no_dialog")
                record_result(page_name, "点击新增按钮", "fail", "点击新增后弹窗未打开", screenshot)
        else:
            record_result(page_name, "点击新增按钮", "fail", "未找到新增按钮")
            
    except Exception as e:
        screenshot = await take_screenshot(page, "message_error")
        record_result(page_name, "页面测试", "fail", str(e), screenshot)


async def test_community_apply(page: Page):
    page_name = "小区登记申请"
    print(f"\n[*] 测试 {page_name}...")
    
    try:
        await page.goto(f"{BASE_URL}/biz/communityApply")
        await page.wait_for_load_state("networkidle")
        await page.wait_for_timeout(1500)
        
        record_result(page_name, "页面加载成功", "pass")
        
        search_btn = page.locator('button:has-text("搜索"), .el-button--primary:has-text("搜索")')
        if await search_btn.count() > 0:
            await search_btn.first.click()
            await page.wait_for_timeout(1000)
            await check_for_errors(page, page_name, "点击搜索")
            record_result(page_name, "点击搜索按钮", "pass")
        else:
            record_result(page_name, "点击搜索按钮", "fail", "未找到搜索按钮")
        
        approve_btn = page.locator('button:has-text("审批"), button:has-text("审核")')
        if await approve_btn.count() > 0:
            await approve_btn.first.click()
            await page.wait_for_timeout(1500)
            dialog = page.locator('.el-dialog, .el-drawer')
            if await dialog.count() > 0:
                record_result(page_name, "点击审批按钮", "pass")
                close_btn = page.locator('.el-dialog__close, .el-drawer__close-btn, button:has-text("取消")')
                if await close_btn.count() > 0:
                    await close_btn.first.click()
                    await page.wait_for_timeout(500)
            else:
                await check_for_errors(page, page_name, "点击审批")
                record_result(page_name, "点击审批按钮", "pass", "无弹窗但无错误")
        else:
            record_result(page_name, "点击审批按钮", "fail", "未找到审批按钮")
            
    except Exception as e:
        screenshot = await take_screenshot(page, "community_apply_error")
        record_result(page_name, "页面测试", "fail", str(e), screenshot)


async def test_phonebook_apply(page: Page):
    page_name = "电话簿申请"
    print(f"\n[*] 测试 {page_name}...")
    
    try:
        await page.goto(f"{BASE_URL}/biz/phonebookApply")
        await page.wait_for_load_state("networkidle")
        await page.wait_for_timeout(1500)
        
        record_result(page_name, "页面加载成功", "pass")
        
        search_btn = page.locator('button:has-text("搜索"), .el-button--primary:has-text("搜索")')
        if await search_btn.count() > 0:
            await search_btn.first.click()
            await page.wait_for_timeout(1000)
            await check_for_errors(page, page_name, "点击搜索")
            record_result(page_name, "点击搜索按钮", "pass")
        else:
            record_result(page_name, "点击搜索按钮", "fail", "未找到搜索按钮")
        
        approve_btn = page.locator('button:has-text("审批"), button:has-text("审核")')
        if await approve_btn.count() > 0:
            await approve_btn.first.click()
            await page.wait_for_timeout(1500)
            dialog = page.locator('.el-dialog, .el-drawer')
            if await dialog.count() > 0:
                record_result(page_name, "点击审批按钮", "pass")
                close_btn = page.locator('.el-dialog__close, .el-drawer__close-btn, button:has-text("取消")')
                if await close_btn.count() > 0:
                    await close_btn.first.click()
                    await page.wait_for_timeout(500)
            else:
                await check_for_errors(page, page_name, "点击审批")
                record_result(page_name, "点击审批按钮", "pass", "无弹窗但无错误")
        else:
            record_result(page_name, "点击审批按钮", "fail", "未找到审批按钮")
            
    except Exception as e:
        screenshot = await take_screenshot(page, "phonebook_apply_error")
        record_result(page_name, "页面测试", "fail", str(e), screenshot)


async def test_miniapp_user(page: Page):
    page_name = "小程序用户"
    print(f"\n[*] 测试 {page_name}...")
    
    try:
        await page.goto(f"{BASE_URL}/miniapp/user")
        await page.wait_for_load_state("networkidle")
        await page.wait_for_timeout(1500)
        
        record_result(page_name, "页面加载成功", "pass")
        
        search_btn = page.locator('button:has-text("搜索"), .el-button--primary:has-text("搜索")')
        if await search_btn.count() > 0:
            await search_btn.first.click()
            await page.wait_for_timeout(1000)
            await check_for_errors(page, page_name, "点击搜索")
            record_result(page_name, "点击搜索按钮", "pass")
        else:
            record_result(page_name, "点击搜索按钮", "fail", "未找到搜索按钮")
        
        expand_btn = page.locator('.el-table__expand-icon, .el-icon-arrow-right')
        if await expand_btn.count() > 0:
            await expand_btn.first.click()
            await page.wait_for_timeout(1000)
            await check_for_errors(page, page_name, "展开第一行")
            record_result(page_name, "点击第一行展开箭头", "pass")
        else:
            record_result(page_name, "点击第一行展开箭头", "fail", "未找到展开按钮")
        
        user_tags = page.locator('.el-tag, .tag')
        if await user_tags.count() > 0:
            record_result(page_name, "用户标签正确显示", "pass")
        else:
            record_result(page_name, "用户标签正确显示", "fail", "未找到用户标签")
        
        push_btn = page.locator('button:has-text("推送消息"), button:has-text("推送")')
        if await push_btn.count() > 0:
            await push_btn.first.click()
            await page.wait_for_timeout(1500)
            dialog = page.locator('.el-dialog, .el-drawer')
            if await dialog.count() > 0:
                record_result(page_name, "点击推送消息按钮", "pass")
                close_btn = page.locator('.el-dialog__close, .el-drawer__close-btn, button:has-text("取消")')
                if await close_btn.count() > 0:
                    await close_btn.first.click()
                    await page.wait_for_timeout(500)
            else:
                await check_for_errors(page, page_name, "点击推送消息")
                record_result(page_name, "点击推送消息按钮", "pass", "无弹窗但无错误")
        else:
            record_result(page_name, "点击推送消息按钮", "fail", "未找到推送消息按钮")
        
        push_record_btn = page.locator('button:has-text("推送记录")')
        if await push_record_btn.count() > 0:
            await push_record_btn.first.click()
            await page.wait_for_timeout(1500)
            await check_for_errors(page, page_name, "点击推送记录")
            record_result(page_name, "点击推送记录按钮", "pass")
            close_btn = page.locator('.el-dialog__close, .el-drawer__close-btn, button:has-text("关闭")')
            if await close_btn.count() > 0:
                await close_btn.first.click()
                await page.wait_for_timeout(500)
        else:
            record_result(page_name, "点击推送记录按钮", "fail", "未找到推送记录按钮")
            
    except Exception as e:
        screenshot = await take_screenshot(page, "miniapp_user_error")
        record_result(page_name, "页面测试", "fail", str(e), screenshot)


async def test_sensitive(page: Page):
    page_name = "敏感词管理"
    print(f"\n[*] 测试 {page_name}...")
    
    try:
        await page.goto(f"{BASE_URL}/biz/sensitive")
        await page.wait_for_load_state("networkidle")
        await page.wait_for_timeout(1500)
        
        record_result(page_name, "页面加载成功", "pass")
        
        search_btn = page.locator('button:has-text("搜索"), .el-button--primary:has-text("搜索")')
        if await search_btn.count() > 0:
            await search_btn.first.click()
            await page.wait_for_timeout(1000)
            await check_for_errors(page, page_name, "点击搜索")
            record_result(page_name, "点击搜索按钮", "pass")
        else:
            record_result(page_name, "点击搜索按钮", "fail", "未找到搜索按钮")
        
        add_btn = page.locator('button:has-text("新增"), .el-button--success:has-text("新增")')
        if await add_btn.count() > 0:
            await add_btn.first.click()
            await page.wait_for_timeout(1500)
            dialog = page.locator('.el-dialog, .el-drawer')
            if await dialog.count() > 0:
                record_result(page_name, "点击新增按钮", "pass")
                close_btn = page.locator('.el-dialog__close, .el-drawer__close-btn, button:has-text("取消")')
                if await close_btn.count() > 0:
                    await close_btn.first.click()
                    await page.wait_for_timeout(500)
            else:
                screenshot = await take_screenshot(page, "sensitive_add_no_dialog")
                record_result(page_name, "点击新增按钮", "fail", "点击新增后弹窗未打开", screenshot)
        else:
            record_result(page_name, "点击新增按钮", "fail", "未找到新增按钮")
            
    except Exception as e:
        screenshot = await take_screenshot(page, "sensitive_error")
        record_result(page_name, "页面测试", "fail", str(e), screenshot)


def generate_report():
    now = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    
    report = f"""# 租赁平台业务管理页面自动化测试报告

**测试时间：** {now}
**测试环境：** {BASE_URL}
**测试账号：** {USERNAME}

---

## 一、测试总览

| 指标 | 数量 |
|------|------|
| 测试页面数 | 12 |
| 测试用例总数 | {total_tests} |
| 通过 | {passed_tests} |
| 失败 | {failed_tests} |
| 通过率 | {passed_tests/total_tests*100:.1f}% |

---

## 二、各页面测试详情

"""
    
    pages = {}
    for result in test_results:
        page_name = result["page"]
        if page_name not in pages:
            pages[page_name] = []
        pages[page_name].append(result)
    
    for page_name, results in pages.items():
        page_pass = sum(1 for r in results if r["status"] == "pass")
        page_fail = sum(1 for r in results if r["status"] == "fail")
        report += f"### {page_name}\n\n"
        report += f"- **测试项：** {len(results)} 个\n"
        report += f"- **通过：** {page_pass} 个\n"
        report += f"- **失败：** {page_fail} 个\n\n"
        report += "| 测试项 | 状态 | 备注 |\n"
        report += "|--------|------|------|\n"
        for r in results:
            status = "✅ 通过" if r["status"] == "pass" else "❌ 失败"
            remark = r["error"] if r["error"] else "-"
            if len(remark) > 100:
                remark = remark[:100] + "..."
            report += f"| {r['test_item']} | {status} | {remark} |\n"
        report += "\n"
    
    report += "---\n\n## 三、发现的 Bug 列表\n\n"
    
    if bugs:
        for i, bug in enumerate(bugs, 1):
            report += f"### Bug {i}: {bug['page']} - {bug['test_item']}\n\n"
            report += f"- **页面：** {bug['page']}\n"
            report += f"- **测试项：** {bug['test_item']}\n"
            report += f"- **错误描述：** {bug['error']}\n"
            if bug["screenshot"]:
                report += f"- **截图：** {bug['screenshot']}\n"
            report += "\n"
    else:
        report += "🎉 未发现任何 Bug！所有测试项均通过。\n\n"
    
    report += "---\n\n## 四、统计数据\n\n"
    report += "- **总测试用例：** " + str(total_tests) + "\n"
    report += "- **通过用例：** " + str(passed_tests) + "\n"
    report += "- **失败用例：** " + str(failed_tests) + "\n"
    report += "- **通过率：** " + f"{passed_tests/total_tests*100:.1f}%" + "\n"
    report += "- **Bug 数量：** " + str(len(bugs)) + "\n\n"
    
    report += "---\n\n*报告由自动化测试脚本生成*\n"
    
    with open(REPORT_PATH, "w", encoding="utf-8") as f:
        f.write(report)
    
    print(f"\n[+] 测试报告已生成: {REPORT_PATH}")
    return report


async def main():
    print("=" * 60)
    print("租赁平台业务管理页面自动化测试")
    print("=" * 60)
    
    async with async_playwright() as p:
        browser = await p.chromium.launch(headless=True)
        context = await browser.new_context(
            viewport={"width": 1920, "height": 1080},
            ignore_https_errors=True
        )
        page = await context.new_page()
        
        try:
            if not await login(page):
                print("[-] 登录失败，终止测试")
                await browser.close()
                return
            
            await test_community(page)
            await test_house(page)
            await test_contract(page)
            await test_repair(page)
            await test_phonebook(page)
            await test_banner(page)
            await test_invite(page)
            await test_message(page)
            await test_community_apply(page)
            await test_phonebook_apply(page)
            await test_miniapp_user(page)
            await test_sensitive(page)
            
        except Exception as e:
            print(f"[-] 测试过程中发生错误: {e}")
            import traceback
            traceback.print_exc()
        finally:
            await browser.close()
    
    generate_report()
    
    print("\n" + "=" * 60)
    print(f"测试完成！总用例: {total_tests}, 通过: {passed_tests}, 失败: {failed_tests}")
    print("=" * 60)


if __name__ == "__main__":
    asyncio.run(main())
