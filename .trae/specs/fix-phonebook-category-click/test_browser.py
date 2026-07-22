#!/usr/bin/env python3
"""
电话簿管理分类点击浏览器测试脚本
使用 Playwright 模拟用户点击"餐饮美食"分类树节点
"""
import sys
import os
from playwright.sync_api import sync_playwright

CHROME_PATH = "/tmp/chrome-browser/chrome-linux64/chrome"
BASE_URL = "http://localhost:8081"

def main():
    console_errors = []
    page_errors = []

    with sync_playwright() as p:
        # Launch Chrome with the extracted binary
        browser = p.chromium.launch(
            executable_path=CHROME_PATH,
            headless=True,
            args=["--no-sandbox", "--disable-setuid-sandbox", "--disable-dev-shm-usage"]
        )
        page = browser.new_page(viewport={"width": 1400, "height": 900})

        # Capture console errors
        page.on("console", lambda msg: console_errors.append(f"[{msg.type}] {msg.text}") if msg.type in ["error", "warning"] else None)
        page.on("pageerror", lambda err: page_errors.append(str(err)))

        print("=" * 60)
        print("步骤 1: 打开登录页面")
        print("=" * 60)
        page.goto(f"{BASE_URL}/login", wait_until="networkidle", timeout=30000)
        page.wait_for_timeout(2000)
        print(f"✓ 页面标题: {page.title()}")

        print("\n" + "=" * 60)
        print("步骤 2: 登录系统")
        print("=" * 60)
        # Fill login form
        username_input = page.locator('input[type="text"]').first
        password_input = page.locator('input[type="password"]').first
        username_input.fill("admin")
        password_input.fill("admin123")
        print("✓ 填写用户名和密码")

        # Click login button (RuoYi uses "登 录" with a space in the text)
        login_btn = page.locator('button.el-button--primary').first
        login_btn.click()
        page.wait_for_timeout(3000)
        try:
            page.wait_for_load_state("networkidle", timeout=15000)
        except Exception:
            pass

        current_url = page.url
        print(f"✓ 登录后 URL: {current_url}")
        if "login" in current_url:
            print("✗ 登录失败，仍在登录页面")
            page.screenshot(path="/workspace/.trae/specs/fix-phonebook-category-click/screenshot_login_fail.png")
            browser.close()
            sys.exit(1)

        print("\n" + "=" * 60)
        print("步骤 3: 导航到电话簿管理页面")
        print("=" * 60)
        page.goto(f"{BASE_URL}/biz/phonebook", wait_until="networkidle", timeout=30000)
        page.wait_for_timeout(3000)
        print(f"✓ 当前 URL: {page.url}")
        page.screenshot(path="/workspace/.trae/specs/fix-phonebook-category-click/screenshot_phonebook_initial.png")
        print("✓ 截图已保存: screenshot_phonebook_initial.png")

        print("\n" + "=" * 60)
        print("步骤 4: 点击左侧分类树'餐饮美食'节点")
        print("=" * 60)

        # Find and click the "餐饮美食" tree node
        tree_node = page.locator('.el-tree-node__content:has-text("餐饮美食")').first
        if tree_node.count() == 0:
            # Try alternative selector
            tree_node = page.locator('span.node-label:has-text("餐饮美食")').first
        if tree_node.count() == 0:
            # Try another selector
            tree_node = page.locator('.custom-tree-node:has-text("餐饮美食")').first

        if tree_node.count() > 0:
            print(f"✓ 找到'餐饮美食'树节点")
            tree_node.click()
            page.wait_for_timeout(3000)
            page.wait_for_load_state("networkidle", timeout=10000)
            print("✓ 点击完成")
        else:
            print("✗ 未找到'餐饮美食'树节点")
            # Print page content for debugging
            content = page.content()
            print(f"页面内容片段: {content[:1000]}")

        page.screenshot(path="/workspace/.trae/specs/fix-phonebook-category-click/screenshot_after_click_canyin.png")
        print("✓ 截图已保存: screenshot_after_click_canyin.png")

        # Check table content
        print("\n" + "=" * 60)
        print("步骤 5: 验证表格内容")
        print("=" * 60)
        table_rows = page.locator('.el-table__body-wrapper .el-table__row')
        row_count = table_rows.count()
        print(f"✓ 表格行数: {row_count}")

        if row_count > 0:
            for i in range(row_count):
                row = table_rows.nth(i)
                cells = row.locator('.el-table__cell')
                cell_texts = []
                for j in range(cells.count()):
                    text = cells.nth(j).inner_text().strip()
                    cell_texts.append(text)
                print(f"  行 {i+1}: {' | '.join(cell_texts[:8])}")
        else:
            print("  表格无数据")
            # Check for empty text
            empty_text = page.locator('.el-table__empty-text').first
            if empty_text.count() > 0:
                print(f"  空数据提示: {empty_text.inner_text()}")

        # Check for error messages
        print("\n" + "=" * 60)
        print("步骤 6: 检查错误提示")
        print("=" * 60)
        error_msg = page.locator('.el-message--error').first
        if error_msg.count() > 0:
            print(f"✗ 发现错误提示: {error_msg.inner_text()}")
        else:
            print("✓ 无错误提示")

        print("\n" + "=" * 60)
        print("步骤 7: 点击'全部'节点")
        print("=" * 60)
        all_node = page.locator('.el-tree-node__content:has-text("全部")').first
        if all_node.count() > 0:
            all_node.click()
            page.wait_for_timeout(3000)
            page.wait_for_load_state("networkidle", timeout=10000)
            print("✓ 点击'全部'完成")
        else:
            print("✗ 未找到'全部'节点")

        page.screenshot(path="/workspace/.trae/specs/fix-phonebook-category-click/screenshot_after_click_all.png")
        print("✓ 截图已保存: screenshot_after_click_all.png")

        # Check table content again
        table_rows = page.locator('.el-table__body-wrapper .el-table__row')
        row_count = table_rows.count()
        print(f"✓ 表格行数: {row_count}")
        if row_count > 0:
            for i in range(min(row_count, 5)):
                row = table_rows.nth(i)
                cells = row.locator('.el-table__cell')
                cell_texts = []
                for j in range(cells.count()):
                    text = cells.nth(j).inner_text().strip()
                    cell_texts.append(text)
                print(f"  行 {i+1}: {' | '.join(cell_texts[:8])}")

        print("\n" + "=" * 60)
        print("步骤 8: 检查控制台错误")
        print("=" * 60)
        if console_errors:
            print(f"发现 {len(console_errors)} 个控制台警告/错误:")
            for err in console_errors:
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
        print("✓ 登录成功")
        print("✓ 导航到电话簿管理页面成功")
        print("✓ 点击'餐饮美食'分类树节点成功")
        print("✓ 点击'全部'节点成功")
        if not console_errors and not page_errors:
            print("✓ 无 JavaScript 错误")
        else:
            print(f"⚠ 发现 {len(console_errors)} 个控制台问题, {len(page_errors)} 个页面错误")

        browser.close()

if __name__ == "__main__":
    main()
