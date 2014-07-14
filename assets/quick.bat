chdir C:\Users\dm199_000\Desktop\Coding\Android\Projects\AncientEmpires\assets\
del /q games.zip
chdir games\
"C:\Program Files\WinRAR\winrar.exe" a -r ..\games.zip

chdir ..\
copy games.zip ..\..\AncientEmpires-java\assets\games.zip

rd /s /q ..\..\AncientEmpires-java\assets\games\
xcopy /e /i /y games ..\..\AncientEmpires-java\assets\games

::pause