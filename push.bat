@echo off
for /f "delims=" %%t in ('git symbolic-ref --short -q HEAD') do set current_branch=%%t
SET gerrit_branch=HEAD:refs/for/%current_branch%
git push origin %gerrit_branch%