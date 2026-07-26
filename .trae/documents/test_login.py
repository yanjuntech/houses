#!/usr/bin/env python3
"""
验证前端登录 - Playwright 浏览器测试
"""
import sys
from playwright.sync_api import sync_playwright

CHROME_PATH = "/tmp/chrome-browser/chrome-linux64/chrome"
BASE_URL = "http://localhost:8081"

def main():
    console_errors = []
    page_errors = []

    with sync_playwright() as p:
        browser = p.chromium.launch(
            executable_path=CHROME_PATH,
            headless=True,
            args=["--no-sandbox", "--disable-setuid-sandbox", "--disable-dev-shm-usage"]
        )
        page = browser.new_page(viewport={"width": 1400, "height": 900})

        page.on("console", lambda msg: console_errors.append(f"[{msg.type}] {msg.text}") if msg.type in ["error", "warning"] else None)
        page.on("pageerror", lambda err: page_errors.append(str(err)))

        print("=" * 60)
        print("步骤 1: 打开登录页面")
        print("=" * 60)
        page.goto(f"{BASE_URL}/login", wait_until="networkidle", timeout=30000)
        page.wait_for_timeout(2000)
        title = page.title()
        print(f"✓ 页面标题: {title}")

        print("\n" + "=" * 60)
        print("步骤 2: 登录系统 (admin/admin123)")
        print("=" * 60)
        username_input = page.locator('input[type="text"]').first
        password_input = page.locator('input[type="password"]').first
        username_input.fill("admin")
        password_input.fill("admin123")
        print("✓ 填写用户名和密码")

        # Check if captcha input exists
        captcha_input = page.locator('input[placeholder*="验证码"]')
        if captcha_input.count() > 0:
            print("⚠ 检测到验证码输入框，验证码应该已关闭但页面仍显示")
            captcha_input.fill("8888")

        login_btn = page.locator('button.el-button--primary').first
        login_btn.click()
        page.wait_for_timeout(3000)
        try:
            page.wait_for_load_state("networkidle", timeout=15000)
        except Exception:
            pass

        current_url = page.url
        print(f"✓ 登录后 URL: {current_url}")

        # Screenshot
        page.screenshot(path="/workspace/.trae/documents/login_after.png")
        print("✓ 截图保存: login_after.png")

        # Check for error messages
        error_msg = page.locator('.el-message--error').first
        if error_msg.count() > 0:
            try:
                print(f"✗ 错误提示: {error_msg.inner_text()}")
            except Exception:
                pass
        else:
            print("✓ 无错误提示")

        # Verify login success
        login_success = False
        if "/index" in current_url or current_url.endswith("/index") or "/dashboard" in current_url:
            login_success = True
            print("✓ 登录成功 - URL 包含 /index")
        elif "login" not in current_url:
            login_success = True
            print(f"✓ 登录成功 - 已离开登录页面: {current_url}")
        else:
            print("✗ 登录失败 - 仍在登录页面")

        print("\n" + "=" * 60)
        print("步骤 3: 检查页面内容")
        print("=" * 60)
        body_text = page.locator('body').inner_text()
        if "若依管理系统" in body_text:
            print("✓ 页面包含'若依管理系统'")
        if "首页" in body_text or "仪表盘" in body_text or "dashboard" in body_text.lower():
            print("✓ 页面包含首页/仪表盘内容")

        print("\n" + "=" * 60)
        print("步骤 4: 检查控制台错误")
        print("=" * 60)
        if console_errors:
            print(f"发现 {len(console_errors)} 个控制台警告/错误:")
            for err in console_errors[:10]:
                print(f"  {err}")
        else:
            print("✓ 无控制台错误")

        if page_errors:
            print(f"发现 {len(page_errors)} 个页面错误:")
            for err in page_errors:
                print(f"  {err}")
        else:
            print("✓ 无页面错误")

        print("\n" + "=" * 60)
        print("测试总结")
        print("=" * 60)
        if login_success:
            print("✅ 前端登录验证成功!")
        else:
            print("❌ 前端登录验证失败")

        browser.close()
        return 0 if login_success else 1

if __name__ == "__main__":
    sys.exit(main())
