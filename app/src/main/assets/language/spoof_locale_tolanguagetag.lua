function after(hook, param)
	local res = param:getResult()
	if res == nil then
	    return false
	end

	local fake = param:getSetting("zone.language.tag", "is-IS")
	param:setResult(fake)
	return true, res, fake
end