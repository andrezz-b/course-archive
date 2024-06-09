"use client";

import * as React from "react";
import { Check, ChevronsUpDown } from "lucide-react";

import { cn } from "@/lib/utils";
import { Button } from "@/components/ui/button";
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList,
} from "@/components/ui/command";
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover";

interface ComboboxProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  options: Array<{ label: string; value: string }>;
  onValueChange: (value: string) => void;
  value: string;
  placeholder?: string;
}

export const Combobox = React.forwardRef<HTMLButtonElement, ComboboxProps>(
  ({ options, onValueChange, value, placeholder = "Select item...", className, ...props }, ref) => {
    const [open, setOpen] = React.useState(false);
    const [selectedValue, setSelectedValue] = React.useState<string>(value);

    React.useEffect(() => {
      if (JSON.stringify(selectedValue) !== JSON.stringify(value)) {
        setSelectedValue(value);
      }
    }, [value, selectedValue]);

    return (
      <Popover open={open} onOpenChange={setOpen}>
        <PopoverTrigger asChild>
          <Button
            variant="outline"
            {...props}
            role="combobox"
            aria-expanded={open}
            className={cn("flex w-full justify-between", className)}
            ref={ref}
          >
            {selectedValue ? (
              options.find((option) => option.value === selectedValue)?.label
            ) : (
              <span>{placeholder}</span>
            )}
            <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50 justify-self-end" />
          </Button>
        </PopoverTrigger>
        <PopoverContent className="w-[200px] p-0">
          <Command>
            <CommandInput placeholder="Search..." />
            <CommandList>
              <CommandEmpty>No framework found.</CommandEmpty>
              <CommandGroup>
                {options.map((option) => (
                  <CommandItem
                    key={option.value}
                    value={option.value}
                    onSelect={(currentValue) => {
                      onValueChange(currentValue === selectedValue ? "" : currentValue);
                      setOpen(false);
                    }}
                  >
                    <Check
                      className={cn(
                        "mr-2 h-4 w-4",
                        selectedValue === option.value ? "opacity-100" : "opacity-0",
                      )}
                    />
                    {option.label}
                  </CommandItem>
                ))}
              </CommandGroup>
            </CommandList>
          </Command>
        </PopoverContent>
      </Popover>
    );
  },
);
