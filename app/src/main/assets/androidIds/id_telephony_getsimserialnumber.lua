function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		return false
	end

	local fake = param:getSettingReMap("unique.gsm.sim.serial", "phone.simserial", "000000000")
	param:setResult(fake)
	return true, ret, fake
end