function soNumero(evt, campo)
{
	if (navigator.appCodeName == 'Mozilla' && (navigator.appName == 'Netscape' || navigator.appName == 'Opera'))
	{
		if (evt.which)
		{
			if ((evt.which < 48 || evt.which > 57) && evt.which != 8 && evt.which != 9)
			{
				if(navigator.appName == 'Opera') // Opera
				{
					if((evt.which == 86 || evt.which == 67) && evt.ctrlKey) // Ctrl + V ou Ctrl + C
					{
						return true;
					}
				}
				else // Firefox, Chrome, Safari
				{
					if((evt.which == 118 || evt.which == 99) && evt.ctrlKey) // Ctrl + V ou Ctrl + C
					{
						return true;
					}
				}	
				return false;
			}
		}
	}
	else // IE
	{
		if (evt.keyCode < 48 || evt.keyCode > 57)
		{
			return false;
		}
	}
	return true;
}