// Объект ID элементов
var Elements = new Object();
 
// Рамка вокруг элемента
function flashBorder(elmId, flagFlash)
{
Elements[elmId] = flagFlash;

var borderPattern = false;
Flash = setInterval(setBorder, 700);

		function setBorder()
		{		
			for (key in Elements) 
			{
				var elm = document.getElementById(key);
				if ( Elements[key] == "true")
				{
					if (borderPattern)
					{
					borderPattern = false;
					elm.style.border = "solid transparent";
					elm.style.borderWidth = "4px";
					}
					else
					{
					borderPattern = true;
					elm.style.border = "solid Red";
					elm.style.borderWidth = "4px";
					}
				}
				else
				{
					elm.style.border = "";
				}					
			}
		//clearInterval(Flash);
		}
} 

// Отображение подсказки для текущего этапа урока, индекс должен начинаться с 0
function showToastFromHTML(lsnStage){
Android.showToastFromHTML(lsnStage);
}

// Конец урока
function showDialogFromHTML(){
Android.showDialogFromHTML();
}

