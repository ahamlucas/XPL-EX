function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		return false
	end

	local fake = param:getSettingReMap("zone.country.iso", "phone.countryiso", "IS")
	param:setResult(fake)
	return true, ret, fake
end